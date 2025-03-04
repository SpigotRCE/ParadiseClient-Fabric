
package io.github.spigotrce.paradiseclientfabric.chatroom.server.exception;

import java.util.UUID;

public class UserAlreadyVerifiedException extends Exception {
    public UserAlreadyVerifiedException(UUID id) {
        super("User '" + id + "' is already verified.");
    }

    public UserAlreadyVerifiedException() {
    }
}
