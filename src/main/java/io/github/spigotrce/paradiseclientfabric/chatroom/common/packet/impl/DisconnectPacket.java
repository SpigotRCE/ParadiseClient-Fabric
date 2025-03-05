package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler.AbstractPacketHandler;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class DisconnectPacket extends Packet {
    private String message = "";

    public DisconnectPacket() {
    }

    public DisconnectPacket(String message) {
        this.message = message;
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
    public void handle(AbstractPacketHandler handler) {
        handler.handle(this);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
