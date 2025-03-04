package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class HandshakeResponsePacket extends Packet {
    private String username;
    private boolean success;

    public HandshakeResponsePacket(String username, boolean success) {
        this.username = username;
        this.success = success;
    }

    public HandshakeResponsePacket() {
        this.username = null;
        this.success = false;
    }

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public Packet encode(ByteBuf buffer) {
        buffer.writeByte(success? 1 : 0);
        buffer.writeCharSequence(username, Charset.defaultCharset());
        return this;
    }

    @Override
    public Packet decode(ByteBuf buffer) {
        success = buffer.readByte() == 1;
        username = buffer.readCharSequence(buffer.readableBytes(), Charset.defaultCharset()).toString();
        return this;
    }

    @Override
    public Packet create() {
        return new HandshakeResponsePacket();
    }
}
