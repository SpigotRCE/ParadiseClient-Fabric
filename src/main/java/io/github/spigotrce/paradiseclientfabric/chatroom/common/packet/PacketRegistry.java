package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.exception.BadPacketException;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.DisconnectPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakeResponsePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.MessagePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketRegistry {
    private static final Map<Integer, Supplier<Packet>> packetMap = new HashMap<>();

    public static void registerPackets() {
        registerPacket(0x00, HandshakePacket::new);
        registerPacket(0x01, HandshakeResponsePacket::new);
        registerPacket(0x02, DisconnectPacket::new);
        registerPacket(0x03, MessagePacket::new);
    }

    private static void registerPacket(int id, Supplier<Packet> supplier) {
        packetMap.put(id, supplier);
    }

    public static Packet createAndDecode(int id, ByteBuf buf) {
        Supplier<Packet> supplier = packetMap.get(id);
        if (supplier != null) {
            Packet packet = supplier.get();
            packet.decode(buf);
            return packet;
        }
        return null;
    }

    public static void sendPacket(Packet packet, Channel channel) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(getPacketId(packet));
        packet.encode(buf);
        channel.writeAndFlush(buf);
    }

    public static int getPacketId(Packet packet) {
        for (Map.Entry<Integer, Supplier<Packet>> entry : packetMap.entrySet())
            if (packet.getClass().equals(entry.getValue().get().getClass()))
                return entry.getKey();
        throw new BadPacketException("Packet not registered: " + packet.getClass().getName());
    }
}
