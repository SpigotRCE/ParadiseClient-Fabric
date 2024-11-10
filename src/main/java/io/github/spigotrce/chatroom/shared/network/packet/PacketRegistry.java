package io.github.spigotrce.chatroom.shared.network.packet;

import io.github.spigotrce.chatroom.shared.network.packet.impl.DisconnectPacket;
import io.github.spigotrce.chatroom.shared.network.packet.impl.HandshakePacket;
import io.github.spigotrce.chatroom.shared.network.packet.impl.MessagePacket;

public class PacketRegistry {
    public static void init() {
        PacketFactory.registerPacket("handshake", HandshakePacket::new);
        PacketFactory.registerPacket("message", MessagePacket::new);
        PacketFactory.registerPacket("disconnect", DisconnectPacket::new);
    }
}
