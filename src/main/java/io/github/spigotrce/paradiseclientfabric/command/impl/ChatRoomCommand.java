package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.minecraft.client.MinecraftClient;

public class ChatRoomCommand extends Command {
    public ChatRoomCommand(MinecraftClient minecraftClient) {
        super("chatroom", "Connects you to a chatroom", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return null;
    }
}
