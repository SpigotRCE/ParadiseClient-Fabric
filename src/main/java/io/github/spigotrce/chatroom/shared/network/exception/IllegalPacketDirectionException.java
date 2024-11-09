package io.github.spigotrce.chatroom.shared.network.exception;

public class IllegalPacketDirectionException extends RuntimeException {
    public IllegalPacketDirectionException() {
        super();
    }

    public IllegalPacketDirectionException(String s) {
        super(s);
    }
}