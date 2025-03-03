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

    @Override
    public void onReload() {
        System.out.println("[Error] It is not supposed to reload the config on it's own! ");
    }
}
