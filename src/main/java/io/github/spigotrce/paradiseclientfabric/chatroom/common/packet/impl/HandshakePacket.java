package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler.AbstractPacketHandler;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class HandshakePacket extends Packet {
    private String token = "";

    public HandshakePacket() {
    }

    public HandshakePacket(String token) {
        this.token = token;
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
    public void handle(AbstractPacketHandler handler) {
        handler.handle(this);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
