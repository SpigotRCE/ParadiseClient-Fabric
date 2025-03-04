package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class MessagePacket extends Packet {
    private String message;

    public MessagePacket() {
    }

    @Override
    public int getID() {
        return 3;
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
        return new MessagePacket();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
