package io.github.spigotrce.paradiseclientfabric.chatroom.client;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.DisconnectPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakeResponsePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.MessagePacket;
import io.netty.channel.ChannelHandlerContext;

// TODO: do the same for the server as well
// TODO: make it better ffs
public class ClientPacketHandler {
    public static boolean isAuthenticated = false;
    public static void handleHandshakeResponse(HandshakeResponsePacket packet, ChannelHandlerContext ctx) {
        if (isAuthenticated)
            throw new IllegalStateException("Already authenticated");
        isAuthenticated = packet.isSuccess();
        if (isAuthenticated) {
            System.out.println("Connected to chat server");
        } else {
            // we are using getUsername for disconnection message, not a great idea but still why not
            System.out.println("Failed to connect to chat server: " + packet.getUsername());
            ctx.close();
            return;
        }

        System.out.println("Welcome back: " + packet.getUsername());
    }

    public static void handleMessagePacket(MessagePacket packet, ChannelHandlerContext ctx) {
        if (!isAuthenticated)
            throw new IllegalStateException("Not authenticated");
        System.out.println(packet.getMessage());
    }

    public static void handleDisconnectPacket(DisconnectPacket packet, ChannelHandlerContext ctx) {
        System.out.println("Disconnected for: " + packet.getMessage());
        ctx.close();
    }
}
