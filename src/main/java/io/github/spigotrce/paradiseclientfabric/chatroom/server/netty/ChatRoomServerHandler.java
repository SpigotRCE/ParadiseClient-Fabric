package io.github.spigotrce.paradiseclientfabric.chatroom.server.netty;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.exception.BadPacketException;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.PacketRegistry;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.DisconnectPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.MessagePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Logging;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Arrays;

public class ChatRoomServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public UserModel userModel;
    public boolean isAuthenticated = false;

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                future -> channels.add(ctx.channel()));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        int id = msg.readInt();
        Packet packet = PacketRegistry.createPacket(id);

        switch (packet) {
            case HandshakePacket handshakePacket -> {
                if (isAuthenticated)
                    throw new IllegalStateException("User already authenticated");

                String uuid = Arrays.asList(handshakePacket.getToken().split("\\.")).get(0);
                String auth = Arrays.asList(handshakePacket.getToken().split("\\.")).get(1);
                // TODO: do authentication

                userModel = new UserModel("username", "email", handshakePacket.getToken());
                Logging.info("Connection: " + userModel.username() + "/" + ctx.channel().remoteAddress());

                MessagePacket messagePacket = new MessagePacket();
                messagePacket.setMessage(userModel.username() + " joined the chat");
                channels.forEach(channel -> sendPacket(packet, channel));

            }
            case MessagePacket messagePacket -> {
                if (!isAuthenticated)
                    throw new IllegalStateException("User not authenticated");

                channels.forEach(channel -> sendPacket(messagePacket, channel));
            }
            case DisconnectPacket ignored -> ctx.close();
            case null, default -> throw new BadPacketException(id);
        }
        msg.release();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        channels.remove(ctx.channel());
        if (userModel != null) {
            Logging.info("Disconnection: " + userModel.username() + "/" + ctx.channel().remoteAddress());
            MessagePacket packet = new MessagePacket();
            packet.setMessage(userModel.username() + " left the chat");
            channels.forEach(channel -> sendPacket(packet, channel));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Logging.error("Exception caught on netty thread", cause);
        DisconnectPacket packet = ((DisconnectPacket) PacketRegistry.createPacket(0));
        packet.setMessage("Error in netty thread, check server console.");
        sendPacket(packet, ctx.channel());
        ctx.close();
    }

    public void sendPacket(Packet packet, Channel channel) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(packet.getID());
        packet.encode(buf);
        channel.writeAndFlush(buf);
    }
}
