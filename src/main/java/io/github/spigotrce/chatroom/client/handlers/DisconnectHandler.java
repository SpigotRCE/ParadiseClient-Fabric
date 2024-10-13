package io.github.spigotrce.chatroom.client.handlers;

import io.github.spigotrce.chatroom.shared.PacketHandler;
import org.jetbrains.annotations.Nullable;

public class DisconnectHandler implements PacketHandler<Object> {

    @Override
    public void handle(@Nullable Object o) {
        System.out.println("Server disconnected!");
    }
}
