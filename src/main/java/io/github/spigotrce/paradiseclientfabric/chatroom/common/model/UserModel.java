package io.github.spigotrce.paradiseclientfabric.chatroom.common.model;

public record UserModel(String username, String email, String token) {

    public UserModel withUsername(String username) {
        return new UserModel(username, email, token);
    }

    public UserModel withEmail(String email) {
        return new UserModel(username, email, token);
    }

    public UserModel withToken(String token) {
        return new UserModel(username, email, token);
    }
}
