package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

public class ChatRoomCommand extends Command {
    public ChatRoomCommand(MinecraftClient minecraftClient) {
        super("chatroom", "Connects you to a chatroom", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
        return null;
    }
}
