package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class HandshakeSuccessPacket extends Packet {
    public HandshakeSuccessPacket() {
    }

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void encode(ByteBuf buffer) {
    }

    @Override
    public void decode(ByteBuf buffer) {
    }

    @Override
    public Packet create() {
        return new HandshakeSuccessPacket();
    }
}
