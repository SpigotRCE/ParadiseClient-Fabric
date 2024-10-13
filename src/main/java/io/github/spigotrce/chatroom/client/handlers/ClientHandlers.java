package io.github.spigotrce.chatroom.client.handlers;

import io.github.spigotrce.chatroom.shared.PacketHandler;

public enum ClientHandlers {

    DISCONNECT(new DisconnectHandler());

    public PacketHandler<Object> handler;

    private ClientHandlers(PacketHandler<Object> handler) {
        this.handler = handler;
    }

}
