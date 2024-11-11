package io.github.spigotrce.chatroom.shared.network.packet;

import io.github.spigotrce.chatroom.shared.network.packet.impl.DisconnectPacket;
import io.github.spigotrce.chatroom.shared.network.packet.impl.HandshakePacket;
import io.github.spigotrce.chatroom.shared.network.packet.impl.HandshakeResponsePacket;
import io.github.spigotrce.chatroom.shared.network.packet.impl.MessagePacket;

public abstract class AbstractPacketHandler {
    public void handle(Packet packet) {
        throw new IllegalStateException("Use of raw packet apply");
    }

    public abstract void handle(DisconnectPacket packet) throws Exception;
    public abstract void handle(HandshakePacket packet) throws Exception;
    public abstract void handle(HandshakeResponsePacket packet) throws Exception;
    public abstract void handle(MessagePacket packet) throws Exception;
}
