package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.DisconnectPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakeResponsePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.MessagePacket;

public abstract class AbstractPacketHandler {
    public void handle(Packet packet) {
    }

    public void handle(HandshakePacket packet) {
    }

    public void handle(HandshakeResponsePacket packet) {
    }

    public void handle(DisconnectPacket packet) {
    }

    public void handle(MessagePacket packet) {
    }
}
