package io.github.spigotrce.paradiseclientfabric.chatroom.client.handler;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler.AbstractPacketHandler;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.DisconnectPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakeResponsePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.MessagePacket;
import io.netty.channel.Channel;

public class ClientPacketHandler extends AbstractPacketHandler {
    private boolean isAuthenticated = false;
    private String username;

    public ClientPacketHandler(Channel channel) {
        super(channel);
    }

    @Override
    public void handle(HandshakeResponsePacket packet) {
        if (isAuthenticated) return;
        isAuthenticated = packet.isSuccess();
        username = packet.getUsername();
        if (isAuthenticated)
            System.out.println("Connected as " + username);
        else
            System.out.println("Disconnected before login: " + username);
    }

    @Override
    public void handle(DisconnectPacket packet) {
        System.out.println("Disconnected from server: " + packet.getMessage());
        channel.close();
    }

    @Override
    public void handle(MessagePacket packet) {
        System.out.println(packet.getMessage());
    }
}
