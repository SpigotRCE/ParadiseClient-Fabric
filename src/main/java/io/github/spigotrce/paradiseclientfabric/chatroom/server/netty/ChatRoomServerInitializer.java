package io.github.spigotrce.paradiseclientfabric.chatroom.server.netty;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.netty.Delimiter;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Main;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

public class ChatRoomServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public ChatRoomServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(sslCtx.newHandler(ch.alloc()));

        int maxPacketSize = 8192;
        pipeline.addLast(new DelimiterBasedFrameDecoder(maxPacketSize, Delimiter.packetDelimiter()));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());

        pipeline.addLast(new ChatRoomServerHandler());

        if (Main.CONFIG.getServer().useHAProxy())
            pipeline.addFirst(new HAProxyMessageDecoder());
    }
}
