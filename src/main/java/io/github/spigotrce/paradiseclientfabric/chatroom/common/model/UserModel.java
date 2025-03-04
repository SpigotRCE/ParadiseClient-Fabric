package io.github.spigotrce.paradiseclientfabric.chatroom.common.model;

import java.sql.Date;
import java.util.UUID;

public record UserModel(long discordID, UUID uuid, Date dateOfRegistration, String username, String email, String token) {

    public UserModel withDiscordID(long discordID) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token);
    }

    public UserModel withUUID(UUID uuid) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token);
    }

    public UserModel withDateOfRegistration(Date dateOfRegistration) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token);
    }

    public UserModel withUsername(String username) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token);
    }

    public UserModel withEmail(String email) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token);
    }

    public UserModel withToken(String token) {
        return new UserModel(discordID, uuid, dateOfRegistration, username, email, token);
    }
}
