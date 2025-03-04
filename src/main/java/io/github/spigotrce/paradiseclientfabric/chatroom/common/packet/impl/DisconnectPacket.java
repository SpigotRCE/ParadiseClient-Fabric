package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class DisconnectPacket extends Packet {
    private String message;

    public DisconnectPacket() {
    }

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeCharSequence(message, Charset.defaultCharset());
    }

    @Override
    public void decode(ByteBuf buffer) {
        message = buffer.readCharSequence(buffer.readableBytes(), Charset.defaultCharset()).toString();
    }

    @Override
    public Packet create() {
        return new DisconnectPacket();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
