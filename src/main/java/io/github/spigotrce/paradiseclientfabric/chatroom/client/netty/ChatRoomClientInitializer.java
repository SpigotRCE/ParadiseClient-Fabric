package io.github.spigotrce.paradiseclientfabric.chatroom.client.netty;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.netty.CommonChannelInitializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ChatRoomClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        CommonChannelInitializer.init(pipeline);

        pipeline.addLast(new ChatRoomClientHandler());
    }
}
