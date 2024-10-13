package io.github.spigotrce.chatroom.server.handlers;

import io.github.spigotrce.chatroom.shared.PacketHandler;

/**
 * <p>The client handler for the disconnect packet.</p>
 */
public class DisconnectHandler implements PacketHandler<Object> {

    @Override
    public void handle(Object o) {
        System.out.println("Client disconnected!");
    }
}
