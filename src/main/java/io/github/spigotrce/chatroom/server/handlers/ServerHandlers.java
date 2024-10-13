package io.github.spigotrce.chatroom.server.handlers;

import io.github.spigotrce.chatroom.shared.PacketHandler;

/**
 * <p>Where to put every handlers of the server.</p>
 */
public enum ServerHandlers {

    DISCONNECT(new DisconnectHandler());

    public PacketHandler packetHandler;

    private ServerHandlers(PacketHandler packetHandler) {
        this.packetHandler = packetHandler;
    }


}
