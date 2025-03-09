package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler.AbstractPacketHandler;
import io.netty.buffer.ByteBuf;

public class KeepAlivePacket extends Packet {
    private int id;

    public KeepAlivePacket(int id) {
        this.id = id;
    }

    public KeepAlivePacket() {
        this.id = 0;
    }

    @Override
    public void encode(ByteBuf buffer) {
        writeInt(buffer, id);
    }

    @Override
    public void decode(ByteBuf buffer) {
        id = readInt(buffer);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
