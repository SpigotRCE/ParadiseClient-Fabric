package io.github.spigotrce.paradiseclientfabric.chatroom.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatRoomClient {
    private final String host;
    private final int port;

    public ChatRoomClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatRoomClientInitializer());

            b.connect(host, port).sync().channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
