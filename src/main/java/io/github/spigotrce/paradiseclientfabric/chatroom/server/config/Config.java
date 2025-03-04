package io.github.spigotrce.paradiseclientfabric.chatroom.server.config;

import java.io.File;
import java.nio.file.Path;

public class Config extends ConfigProvider {
    public Config(Path dataDirectory) {
        super("config.yml", "file-version", dataDirectory.toFile());
    }

    public String getDiscordToken() {
        return getFileConfig().getString("discord_token");
    }

    public int getDiscordServer() {
        return getFileConfig().getInt("discord_server_id");
    }

    public boolean autoVerify() {
        return getFileConfig().getBoolean("auto_verify");
    }

    public String getServerIP() {
        return getFileConfig().getString("server_ip");
    }

    public int getServerPort() {
        return getFileConfig().getInt("server_port");
    }

    public String isHAProxy() {
        return getFileConfig().getString("use_haproxy");
    }

    @Override
    public void onReload() {
    }
}
