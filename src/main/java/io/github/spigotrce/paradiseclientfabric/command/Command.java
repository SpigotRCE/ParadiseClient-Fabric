package io.github.spigotrce.paradiseclientfabric.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

/**
 * Abstract class representing a command in the Paradise Client Fabric mod.
 * This class provides basic functionality for creating commands and managing their properties.
 */
public abstract class Command {
    protected static final int SINGLE_SUCCESS = com.mojang.brigadier.Command.SINGLE_SUCCESS;
    private final MinecraftClient minecraftClient;

    private final String name;
    private final String description;
    private final boolean async;

    /**
     * Constructor for the Command class.
     *
     * @param name            The name of the command.
     * @param description     The description of the command.
     * @param minecraftClient The Minecraft client instance.
     */
    public Command(String name, String description, MinecraftClient minecraftClient) {
        this(name, description, minecraftClient, false);
    }

    /**
     * Constructor for the Command class.
     *
     * @param name            The name of the command.
     * @param description     The description of the command.
     * @param minecraftClient The Minecraft client instance.
     * @param async           Whether the command should be run inside a background thread
     */
    public Command(String name, String description, MinecraftClient minecraftClient, boolean async) {
        this.name = name;
        this.description = description;
        this.minecraftClient = minecraftClient;
        this.async = async;
    }

    /**
     * Protected method to create a literal argument builder for Brigadier.
     *
     * @param name The name of the literal argument.
     * @return A Brigadier literal argument builder.
     */
    protected static LiteralArgumentBuilder<CommandSource> literal(final String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    /**
     * Protected method to create an argument builder for Brigadier.
     *
     * @param name The name of the argument.
     * @param type The type of the argument.
     * @return A Brigadier literal argument builder.
     */
    protected static <T> RequiredArgumentBuilder<CommandSource, T> argument(final String name, final ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    /**
     * Abstract method to build the command using Brigadier's argument builder.
     *
     * @return A Brigadier literal argument builder for the command.
     */
    abstract public LiteralArgumentBuilder<CommandSource> build();

    /**
     * Getter for the command name.
     *
     * @return The name of the command.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the command description.
     *
     * @return The description of the command.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the Minecraft client instance.
     *
     * @return The Minecraft client instance.
     */
    public MinecraftClient getMinecraftClient() {
        return minecraftClient;
    }

    /**
     * Getter for whether command is async.
     *
     * @return Whether command is async.
     */
    public boolean isAsync() {
        return async;
    }

}
