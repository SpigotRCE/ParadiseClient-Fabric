package io.github.spigotrce.paradiseclientfabric.chatroom.client.netty;

import io.github.spigotrce.paradiseclientfabric.chatroom.client.ClientPacketHandler;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.exception.BadPacketException;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.PacketRegistry;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.DisconnectPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakeResponsePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.MessagePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

public class ChatRoomClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        int id = msg.readInt();
        Packet packet = PacketRegistry.createAndDecode(id, msg);

        switch (packet) {
            case HandshakeResponsePacket handshakeResponsePacket ->
                    ClientPacketHandler.handleHandshakeResponse(handshakeResponsePacket, ctx);
            case MessagePacket messagePacket -> ClientPacketHandler.handleMessagePacket(messagePacket, ctx);
            case DisconnectPacket disconnectPacket -> ClientPacketHandler.handleDisconnectPacket(disconnectPacket, ctx);
            case null, default -> throw new BadPacketException("Unknown packet: " + id);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        PacketRegistry.sendPacket(new HandshakePacket(UUID.randomUUID() + ".token_auth"), ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Disconnected from chat server");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        PacketRegistry.sendPacket(new DisconnectPacket(), ctx.channel());
        ctx.close();
    }
}
