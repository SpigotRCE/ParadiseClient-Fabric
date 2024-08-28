package io.github.spigotrce.paradiseclientfabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public abstract class Command {
    protected static final int SINGLE_SUCCESS = com.mojang.brigadier.Command.SINGLE_SUCCESS;
    private final MinecraftClient minecraftClient;

    private final String name;
    private final String description;

    public Command(String name, String description, MinecraftClient minecraftClient) {
        this.name = name;
        this.description = description;
        this.minecraftClient = minecraftClient;
    }

    abstract public LiteralArgumentBuilder<FabricClientCommandSource> build();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public MinecraftClient getMinecraftClient() {
        return minecraftClient;
    }

    protected LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }
}
