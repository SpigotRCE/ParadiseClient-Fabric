package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class DisconnectPacket extends Packet {
    private String message = "";

    public DisconnectPacket() {
    }

    @Override
    public int getID() {
        return 2;
    }

    @Override
    public Packet encode(ByteBuf buffer) {
        buffer.writeCharSequence(message, Charset.defaultCharset());
        return this;
    }

    @Override
    public Packet decode(ByteBuf buffer) {
        message = buffer.readCharSequence(buffer.readableBytes(), Charset.defaultCharset()).toString();
        return this;
    }

    @Override
    public Packet create() {
        return new DisconnectPacket();
    }

    public String getMessage() {
        return message;
    }

    public Packet setMessage(String message) {
        this.message = message;
        return this;
    }
}
