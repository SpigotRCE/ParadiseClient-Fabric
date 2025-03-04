package io.github.spigotrce.paradiseclientfabric.chatroom.server.config;

import io.github.spigotrce.paradiseclientfabric.chatroom.client.DatabaseModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.client.DiscordModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.client.ServerModel;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.nio.file.Path;
import java.security.cert.CertificateException;

public class Config extends ConfigProvider {
    public Config(Path dataDirectory) {
        super("config.yml", "file-version", dataDirectory.toFile());
    }

    public DiscordModel getDiscord() {
        return new DiscordModel(
                getFileConfig().getString("discord_token"),
                getFileConfig().getInt("discord_server_id"),
                getFileConfig().getBoolean("auto_verify")
        );
    }

    public ServerModel getServer() throws CertificateException {
        return new ServerModel(
                getFileConfig().getInt("server_port"),
                getFileConfig().getBoolean("use_haproxy"),
                new SelfSignedCertificate()
        );
    }

    public DatabaseModel getDatabase() {
        return new DatabaseModel(getFileConfig().getString("database_hostname"),
                getFileConfig().getString("database_username"),
                getFileConfig().getString("database_password"),
                getFileConfig().getString("database_name"),
                getFileConfig().getString("database-connection-parameters")
        );
    }

    @Override
    public void onReload() {
    }
}
