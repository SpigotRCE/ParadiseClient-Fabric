package io.github.spigotrce.paradiseclientfabric.chatroom.common.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Delimiter {
    public static ByteBuf[] packetDelimiter() {
        return new ByteBuf[] {
                Unpooled.wrappedBuffer(new byte[] { '\r', '\n' , '\n', 1, 2}),
                Unpooled.wrappedBuffer(new byte[] { '\n', '\r', 1, 2}),
        };
    }
}
