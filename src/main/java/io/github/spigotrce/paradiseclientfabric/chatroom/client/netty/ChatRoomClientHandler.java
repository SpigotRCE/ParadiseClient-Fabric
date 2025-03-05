package io.github.spigotrce.paradiseclientfabric.chatroom.client.netty;

import io.github.spigotrce.paradiseclientfabric.chatroom.client.handler.ClientPacketHandler;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.exception.BadPacketException;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.PacketRegistry;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.DisconnectPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Logging;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

public class ChatRoomClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private ClientPacketHandler packetHandler;
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        Packet packet = PacketRegistry.createAndDecode(msg.readInt(), msg);
        if (packet == null) throw new BadPacketException("Unknown packet");
        packet.handle(packetHandler);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        packetHandler = new ClientPacketHandler(ctx.channel());
        PacketRegistry.sendPacket(new HandshakePacket(UUID.randomUUID() + ".token_auth"), ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Disconnected from chat server");
        packetHandler = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Logging.error("Exception caught on netty thread", cause);
        PacketRegistry.sendPacket(new DisconnectPacket(), ctx.channel());
        ctx.close();
    }
}
