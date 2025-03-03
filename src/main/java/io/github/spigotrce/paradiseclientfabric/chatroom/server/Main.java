package io.github.spigotrce.paradiseclientfabric.chatroom.server;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.exception.UserAlreadyRegisteredException;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.config.Config;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.discord.DiscordBotImpl;

import java.sql.SQLException;

public class Main {
    public static Config CONFIG;

    public static void main(String[] args) {
        DiscordBotImpl.startDiscordBot();
    }

    public static boolean registerNewUser(UserModel user) throws SQLException, UserAlreadyRegisteredException {
        // TODO: register a new user in a database

        // true if the user needs verification, false otherwise
        return true;
    }
}
