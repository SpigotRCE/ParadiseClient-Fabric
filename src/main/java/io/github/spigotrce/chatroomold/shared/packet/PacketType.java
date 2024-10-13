package io.github.spigotrce.chatroomold.shared.packet;

import io.github.spigotrce.chatroomold.shared.packet.impl.common.DisconnectPacket;

public enum PacketType {

    DISCONNECT(DisconnectPacket::new);

    private final PacketCreator creator;

    PacketType(PacketCreator creator) {
        this.creator = creator;
    }

    public Packet create(PacketManager manager) {
        return this.creator.create(manager);
    }
}