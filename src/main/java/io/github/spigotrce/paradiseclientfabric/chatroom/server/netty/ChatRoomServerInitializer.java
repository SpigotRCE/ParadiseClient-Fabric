package io.github.spigotrce.paradiseclientfabric.chatroom.server.netty;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.netty.CommonChannelInitializer;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Main;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;

public class ChatRoomServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        CommonChannelInitializer.init(pipeline);

        pipeline.addLast(new ChatRoomServerHandler());

        if (Main.CONFIG.getServer().useHAProxy())
            pipeline.addFirst(new HAProxyMessageDecoder());
    }
}
