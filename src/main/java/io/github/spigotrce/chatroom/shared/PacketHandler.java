package io.github.spigotrce.chatroom.shared;

/**
 * <p>Handles the packet's behaviors.</p>
 */
public interface PacketHandler {

    public void handle();

    public void write();

}
