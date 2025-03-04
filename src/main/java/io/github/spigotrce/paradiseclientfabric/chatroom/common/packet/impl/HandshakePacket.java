package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class HandshakePacket extends Packet {
    private String token;

    public HandshakePacket() {
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeCharSequence(token, Charset.defaultCharset());
    }

    @Override
    public void decode(ByteBuf buffer) {
        token = buffer.readCharSequence(buffer.readableBytes(), Charset.defaultCharset()).toString();
    }

    @Override
    public Packet create() {
        return new HandshakePacket();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
