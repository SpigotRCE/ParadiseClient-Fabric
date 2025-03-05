package io.github.spigotrce.paradiseclientfabric.chatroom.common.model;

import java.util.Date;
import java.util.UUID;

// token: when sent from the client, it contains the u-u-i-d.token
// ->     when sent from the server/stored on the database it is just the token
public record UserModel(long discordID, UUID uuid, Date dateOfRegistration, String username, String email, String token,
                        boolean verified) {

    public UserModel() {
        this(0, UUID.randomUUID(), new Date(), "", "", "", false);
    }

    public UserModel withDiscordID(long discordID) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token, verified);
    }

    public UserModel withUUID(UUID uuid) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token, verified);
    }

    public UserModel withDateOfRegistration(Date dateOfRegistration) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token, verified);
    }

    public UserModel withUsername(String username) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token, verified);
    }

    public UserModel withEmail(String email) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token, verified);
    }

    public UserModel withToken(String token) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token, verified);
    }

    public UserModel withVerified(boolean verified) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token, verified);
    }
}
