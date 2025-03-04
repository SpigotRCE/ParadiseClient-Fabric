package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.exception.BadPacketException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PacketRegistry {
    private static final Map<Integer, Packet> packetMap = new HashMap<>();

    public static void registerPacket(int id, Supplier<Packet> supplier) {
        packetMap.put(id, supplier.get());
    }

    public static void handlePacket(int id, Consumer<Packet> consumer) throws BadPacketException {
        Packet packet = packetMap.get(id);
        if (packet != null)
            consumer.accept(packet);
        else
            throw new BadPacketException(id);
    }

    public static Packet createPacket(int id) {
        Packet packet = packetMap.get(id);
        if (packet != null)
            return packet.create();
        else
            return null;
    }
}
