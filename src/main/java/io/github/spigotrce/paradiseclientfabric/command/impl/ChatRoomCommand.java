package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import io.github.spigotrce.paradiseclientfabric.command.Command;

public class ChatRoomCommand extends Command {
    public ChatRoomCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return null;
    }
}
