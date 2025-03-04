package io.github.spigotrce.paradiseclientfabric.chatroom.common.netty;

import io.netty.channel.ChannelPipeline;

public class CommonChannelInitializer {
    public static void init(ChannelPipeline pipeline) {
        pipeline.addLast(new LengthPrefixedDecoder());
        pipeline.addLast(new LengthPrefixedEncoder());
    }
}
