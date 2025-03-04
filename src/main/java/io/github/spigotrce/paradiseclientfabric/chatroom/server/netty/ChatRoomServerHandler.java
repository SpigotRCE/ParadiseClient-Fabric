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
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

public class ChatRoomServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public UserModel userModel;
    public boolean isAuthenticated = false;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        int id = msg.readInt();
        Packet packet = PacketRegistry.createAndDecode(id, msg);

        switch (packet) {
            case HandshakePacket handshakePacket -> {
                if (isAuthenticated)
                    throw new IllegalStateException("User already authenticated");

                String uuid = Arrays.asList(handshakePacket.getToken().split("\\.")).get(0);
                String auth = Arrays.asList(handshakePacket.getToken().split("\\.")).get(1);
                // TODO: do authentication

                isAuthenticated = true;

                PacketRegistry.sendPacket(new HandshakeResponsePacket(), ctx.channel());
                userModel = new UserModel(0, UUID.randomUUID(), Date.valueOf(LocalDate.now()), "username", "email", handshakePacket.getToken(), false);
                Logging.info("Connection: " + userModel.username() + ctx.channel().remoteAddress());

                channels.forEach(channel -> PacketRegistry.sendPacket(new MessagePacket().setMessage(userModel.username() + " joined the chat"), channel));
            }
            case MessagePacket messagePacket -> {
                if (!isAuthenticated)
                    throw new IllegalStateException("User not authenticated");

                channels.forEach(channel -> PacketRegistry.sendPacket(messagePacket, channel));
            }
            case DisconnectPacket ignored -> ctx.close();
            case null, default -> throw new BadPacketException(id);
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
            channels.forEach(channel -> PacketRegistry.sendPacket(new MessagePacket().setMessage(userModel.username() + " left the chat"), channel));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Logging.error("Exception caught on netty thread", cause);
        PacketRegistry.sendPacket(new DisconnectPacket().setMessage("Error in netty thread, check server console."), ctx.channel());
        ctx.close();
    }
}
