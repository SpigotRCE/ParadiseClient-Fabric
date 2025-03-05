package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler.AbstractPacketHandler;
import io.netty.buffer.ByteBuf;

public abstract class Packet {
    public abstract void encode(ByteBuf buffer);

    public abstract void decode(ByteBuf buffer);

    public abstract void handle(AbstractPacketHandler handler) throws Exception;
}
