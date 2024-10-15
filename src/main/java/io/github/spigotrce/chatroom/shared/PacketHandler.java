package io.github.spigotrce.chatroom.shared;

import org.jetbrains.annotations.Nullable;

/**
 * <p>Handles the packet's behaviors.</p>
 */
public interface PacketHandler<O> {

    default void handle(@Nullable O o) {
        throw new IllegalStateException("The handler for the current protocol direction doesn't handle reading packets!");
    }

    default void write(@Nullable O o) {
        throw new IllegalStateException("The handler for the current protocol direction doesn't handle writing packets!");
    }

}
