package io.github.spigotrce.paradiseclientfabric.chatroom.client.netty.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class HaProxyMessageHandler extends SimpleChannelInboundHandler<HAProxyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HAProxyMessage msg) throws Exception {
        ctx.channel().attr(
                AttributeKey.valueOf("proxiedAddress")
        ).set(
                new InetSocketAddress(
                        msg.sourceAddress(),
                        msg.sourcePort()
                )
        );
        ctx.fireChannelRead(msg.retain());
    }

}
