package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler.AbstractPacketHandler;
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
    public void encode(ByteBuf buffer) {
        buffer.writeByte(success ? 1 : 0);
        buffer.writeCharSequence(username, Charset.defaultCharset());
    }

    @Override
    public void decode(ByteBuf buffer) {
        success = buffer.readByte() == 1;
        username = buffer.readCharSequence(buffer.readableBytes(), Charset.defaultCharset()).toString();
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
