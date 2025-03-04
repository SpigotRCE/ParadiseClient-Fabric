package io.github.spigotrce.paradiseclientfabric.chatroom.common.exception;

public class BadPacketException extends Exception {
    public BadPacketException(int id) {
        super("Invalid packet id: " + id);
    }

    public BadPacketException(int id, Throwable cause) {
        super("Invalid packet id: " + id, cause);
    }
}
