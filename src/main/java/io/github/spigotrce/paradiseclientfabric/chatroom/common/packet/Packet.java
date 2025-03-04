package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet;

import io.netty.buffer.ByteBuf;

public abstract class Packet {
    public abstract int getID();
    public abstract Packet encode(ByteBuf buffer);
    public abstract Packet decode(ByteBuf buffer);

    public abstract Packet create();
}
