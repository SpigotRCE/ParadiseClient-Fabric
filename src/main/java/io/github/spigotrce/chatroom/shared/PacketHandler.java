package io.github.spigotrce.chatroom.shared;

/**
 * <p>Handles the packet's behaviors.</p>
 */
public interface PacketHandler {

    default void handle() {
        throw new IllegalStateException("The handler for the current protocol direction doesn't handle reading packets!");
    }

    default void write() {
        throw new IllegalStateException("The handler for the current protocol direction doesn't handle writing packets!");
    }

}
