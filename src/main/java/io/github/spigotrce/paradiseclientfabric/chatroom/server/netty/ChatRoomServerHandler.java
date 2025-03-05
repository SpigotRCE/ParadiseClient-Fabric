package io.github.spigotrce.paradiseclientfabric.chatroom.server.netty;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.exception.BadPacketException;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.PacketRegistry;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.DisconnectPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakeResponsePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.MessagePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Logging;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Main;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Arrays;
import java.util.UUID;

public class ChatRoomServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public UserModel userModel;
    public boolean isAuthenticated = false;
    public long lastMessage = 0;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        int id = msg.readInt();
        Packet packet = PacketRegistry.createAndDecode(id, msg);

        switch (packet) {
            case HandshakePacket handshakePacket -> {
                if (isAuthenticated)
                    throw new IllegalStateException("User already authenticated");

                UUID uuid = UUID.fromString(Arrays.asList(handshakePacket.getToken().split("\\.")).getFirst());

                isAuthenticated = Main.authenticate(handshakePacket.getToken());
                if (!isAuthenticated) {
                    PacketRegistry.sendPacket(
                            new HandshakeResponsePacket("Unauthenticated", false)
                            , ctx.channel()
                    );
                    ctx.close();
                    return;
                }


                userModel = Main.DATABASE.getUser(uuid);
                PacketRegistry.sendPacket(
                        new HandshakeResponsePacket(userModel.username(), true)
                        , ctx.channel()
                );
                Logging.info("Connection: " + userModel.username() + ctx.channel().remoteAddress());

                channels.forEach(channel -> PacketRegistry.sendPacket(new MessagePacket(userModel.username() + " joined the chat"), channel));
            }
            case MessagePacket messagePacket -> {
                if (!isAuthenticated)
                    throw new IllegalStateException("User not authenticated");

                if (lastMessage + 5000 > System.currentTimeMillis()) {
                    PacketRegistry.sendPacket(
                            new MessagePacket("Do not spam messages!"),
                            ctx.channel()
                    );
                    return;
                }
                lastMessage = System.currentTimeMillis();
                messagePacket.setMessage(
                        userModel.username() +
                                ">>" +
                                messagePacket.getMessage()
                );
                channels.forEach(channel ->
                        PacketRegistry.sendPacket(
                                messagePacket,
                                channel
                        )
                );

                Logging.info("[Chat] " + messagePacket.getMessage());
            }
            case DisconnectPacket ignored -> ctx.close();
            case null, default -> throw new BadPacketException("Unknown packet id: " + id);
        }
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channels.remove(ctx.channel());
        if (userModel != null) {
            Logging.info("Disconnection: " + userModel.username() + "/" + ctx.channel().remoteAddress());
            channels.forEach(channel -> PacketRegistry.sendPacket(new MessagePacket(userModel.username() + " left the chat"), channel));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Logging.error("Exception caught on netty thread", cause);
        PacketRegistry.sendPacket(new DisconnectPacket("Error in netty thread, check server console."), ctx.channel());
        ctx.close();
    }
}
