package io.github.spigotrce.chatroom.shared.network.packet;

import io.github.spigotrce.chatroom.shared.network.packet.impl.DisconnectPacket;
import io.github.spigotrce.chatroom.shared.network.packet.impl.HandshakePacket;

public abstract class AbstractPacketHandler {
    public void handle(Packet packet) {
        throw new IllegalStateException("Use of raw packet apply");
    }

    public abstract void handle(DisconnectPacket packet);

    public abstract void handle(HandshakePacket packet);
}
