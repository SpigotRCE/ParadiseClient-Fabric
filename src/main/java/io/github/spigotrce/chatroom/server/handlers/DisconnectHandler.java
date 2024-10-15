package io.github.spigotrce.chatroom.server.handlers;

import io.github.spigotrce.chatroom.server.ServerShard;
import io.github.spigotrce.chatroom.shared.PacketHandler;

/**
 * <p>The client handler for the disconnect packet.</p>
 */
public class DisconnectHandler implements PacketHandler<ServerShard> {

    @Override
    public void handle(ServerShard o) {
        System.out.println("Client disconnected!");
    }
}
