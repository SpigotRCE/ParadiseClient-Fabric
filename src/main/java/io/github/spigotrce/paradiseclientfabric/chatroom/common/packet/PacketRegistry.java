package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.DisconnectPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakeSuccessPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.MessagePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketRegistry {
    private static final Map<Integer, Packet> packetMap = new HashMap<>();

    public static void registerPackets() {
        registerPacket(new HandshakePacket());
        registerPacket(new HandshakeSuccessPacket());
        registerPacket(new DisconnectPacket());
        registerPacket(new MessagePacket());
    }

    private static void registerPacket(Packet packet) {
        registerPacket(packet.getID(), packet::create);
    }

    private static void registerPacket(int id, Supplier<Packet> supplier) {
        packetMap.put(id, supplier.get());
    }

    public static Packet createAndDecode(int id, ByteBuf buf) {
        Packet packet = packetMap.get(id);
        if (packet != null)
            return packet.create().decode(buf);
        else
            return null;
    }

    public static void sendPacket(Packet packet, Channel channel) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(packet.getID());
        packet.encode(buf);
        channel.writeAndFlush(buf);
    }
}
