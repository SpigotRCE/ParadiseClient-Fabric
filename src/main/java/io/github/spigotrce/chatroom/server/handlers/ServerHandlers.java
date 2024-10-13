package io.github.spigotrce.chatroom.server.handlers;

import io.github.spigotrce.chatroom.server.ServerShard;
import io.github.spigotrce.chatroom.shared.PacketHandler;

/**
 * <p>Where to put every handlers of the server.</p>
 */
public enum ServerHandlers {

    DISCONNECT(new DisconnectHandler());

    public PacketHandler<ServerShard> packetHandler;

    private ServerHandlers(PacketHandler<ServerShard> packetHandler) {
        this.packetHandler = packetHandler;
    }


}
