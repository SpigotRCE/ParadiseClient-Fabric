package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.netty.buffer.ByteBuf;

public class HandshakeSuccessPacket extends Packet {
    public HandshakeSuccessPacket() {
    }

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public Packet encode(ByteBuf buffer) {
        return this;
    }

    @Override
    public Packet decode(ByteBuf buffer) {
        return this;
    }

    @Override
    public Packet create() {
        return new HandshakeSuccessPacket();
    }
}
