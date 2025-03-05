package io.github.spigotrce.paradiseclientfabric.chatroom.server;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.PacketRegistry;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.config.Config;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.database.MySQLDatabase;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.discord.DiscordBotImpl;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.exception.UserAlreadyRegisteredException;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.exception.UserAlreadyVerifiedException;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.netty.ChatRoomServer;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Main {
    public static Config CONFIG = new Config(new File(System.getProperty("user.dir")).toPath());
    public static MySQLDatabase DATABASE;
    public static String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static void main(String[] args) {
        try {
            CONFIG.load();
        } catch (IOException exception) {
            Logging.error("Unable to load configuration", exception);
            System.exit(1);
        }

        try {
            DATABASE = new MySQLDatabase();
        } catch (SQLException e) {
            Logging.error("Unable to connect to the database", e);
            System.exit(1);
        }

        DiscordBotImpl.startDiscordBot();
        PacketRegistry.registerPackets();

        try {
            ChatRoomServer.startServer(CONFIG.getServer());
        } catch (Exception exception) {
            Logging.error("Error starting chat server...", exception);
            System.exit(1);
        }
    }

    public static boolean registerNewUser(UserModel user) throws SQLException, UserAlreadyRegisteredException {
        DATABASE.insertUser(user);
        return CONFIG.getDiscord().autoVerify();
    }

    public static void verifyUser(UUID uuid) throws SQLException, UserAlreadyVerifiedException {
        UserModel model = DATABASE.getUser(uuid);
        if (model.verified()) throw new UserAlreadyVerifiedException(uuid);
        DATABASE.updateUser(DATABASE.getUser(uuid).withVerified(true));
    }

    public static boolean authenticate(String token) throws SQLException {
        List<String> split = Arrays.asList(token.split("\\."));
        UUID uuid = UUID.fromString(split.get(0));
        String key = split.get(1);
        UserModel model = DATABASE.getUser(uuid);
        return model.token().equals(token);
    }

    public static UserModel generateToken(UserModel model) throws SQLException {
        UserModel newModel = model.withToken(model.uuid() + "." + generateNextString(64));
        DATABASE.updateUser(newModel);
        return newModel;
    }

    private static String generateNextString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        return sb.toString();
    }
}
