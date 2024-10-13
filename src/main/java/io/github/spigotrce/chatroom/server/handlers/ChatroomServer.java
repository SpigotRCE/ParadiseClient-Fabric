package io.github.spigotrce.chatroom.server.handlers;

import io.github.spigotrce.chatroom.shared.PacketProcessor;

public class ChatroomServer extends PacketProcessor<ServerHandlers> {

    /**
     * <p>Creates a new packet processor.</p>
     *
     */
    public ChatroomServer() {
        super(ServerHandlers.values());
    }
}
