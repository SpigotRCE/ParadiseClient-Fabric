package io.github.spigotrce.paradiseclientfabric.chatroom.server.config;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.DatabaseModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.DiscordModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.ServerModel;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.nio.file.Path;
import java.security.cert.CertificateException;

public class Config extends ConfigProvider {
    public Config(Path dataDirectory) {
        super("config.yml", "file-version", dataDirectory.toFile());
    }

    private DiscordModel discordModel;
    private ServerModel serverModel;
    private DatabaseModel databaseModel;

    public DiscordModel getDiscord() {
        return discordModel;
    }

    public ServerModel getServer() {
        return serverModel;
    }

    public DatabaseModel getDatabase() {
        return databaseModel;
    }

    @Override
    public void onReload() {
        discordModel = new DiscordModel(
                getFileConfig().getString("discord_token"),
                getFileConfig().getLong("discord_server_id"),
                getFileConfig().getBoolean("auto_verify"),
                getFileConfig().getLong("verification_channel_id"),
                getFileConfig().getString("webhook_account_logging"),
                getFileConfig().getLong("linked_members_role_id"),
                getFileConfig().getLong("admin_role_id")
        );

        serverModel = new ServerModel(
                getFileConfig().getInt("server_port"),
                getFileConfig().getBoolean("use_haproxy"),
                getFileConfig().getInt("message_cooldown"),
                getFileConfig().getInt("max_message_characters")
        );

        databaseModel = new DatabaseModel(getFileConfig().getString("database_hostname"),
                getFileConfig().getString("database_username"),
                getFileConfig().getString("database_password"),
                getFileConfig().getString("database_name"),
                getFileConfig().getString("database-connection-parameters")
        );
    }
}
