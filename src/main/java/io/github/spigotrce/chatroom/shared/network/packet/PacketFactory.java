package io.github.spigotrce.chatroom.shared.network.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketFactory {
    public static final Map<String, Supplier<Packet>> packetMap = new HashMap<>();

    public static void registerPacket(String id, Supplier<Packet> supplier) {
        packetMap.put(id, supplier);
    }

    public static void decodeAndApply(String id, byte[] data) {
        Supplier<Packet> packetSupplier = packetMap.get(id);
        if (packetSupplier == null) {
            throw new IllegalArgumentException("Unknown packet ID: " + id);
        }
        Packet packet = packetSupplier.get();
        packet.buf = data;
        packet.decode();
    }
}

