package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class HandshakePacket extends Packet {
    private String token = "";

    public HandshakePacket() {
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public Packet encode(ByteBuf buffer) {
        buffer.writeCharSequence(token, Charset.defaultCharset());
        return this;
    }

    @Override
    public Packet decode(ByteBuf buffer) {
        token = buffer.readCharSequence(buffer.readableBytes(), Charset.defaultCharset()).toString();
        return this;
    }

    @Override
    public Packet create() {
        return new HandshakePacket();
    }

    public String getToken() {
        return token;
    }

    public Packet setToken(String token) {
        this.token = token;
        return this;
    }
}
