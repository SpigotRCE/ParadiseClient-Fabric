package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

import java.util.Objects;

/**
 * This class represents a command for griefing actions in a Minecraft client.
 * It extends the {@link Command} class and overrides the {@link #build()} method to define the command structure.
 *
 * @author SpigotRCE
 * @since 1.4
 */
public class GriefCommand extends Command {

    /**
     * Constructs a new instance of {@link GriefCommand}.
     *
     * @param minecraftClient The Minecraft client instance
     */
    public GriefCommand(MinecraftClient minecraftClient) {
        super("grief", "Multiple grief commands", minecraftClient);
    }

    /**
     * Builds the command structure using Brigadier's {@link LiteralArgumentBuilder}.
     *
     * @return The root command node
     */
    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
        return literal(getName())
                .then(literal("tpall")
                        .executes((context) -> {
                            Objects.requireNonNull(getMinecraftClient().getNetworkHandler()).sendChatCommand("tpall");
                            Objects.requireNonNull(getMinecraftClient().getNetworkHandler()).sendChatCommand("etpall");
                            Objects.requireNonNull(getMinecraftClient().getNetworkHandler()).sendChatCommand("minecraft:tp @a @p");
                            Objects.requireNonNull(getMinecraftClient().getNetworkHandler()).sendChatCommand("tp @a @p");
                            return SINGLE_SUCCESS;
                        })
                )
                .then(literal("fill")
                        .then(literal("air")
                                .executes((context) -> {
                                    Objects.requireNonNull(getMinecraftClient().getNetworkHandler()).sendChatCommand("minecraft:fill ~12 ~12 ~12 ~-12 ~-12 ~-12 air");
                                    return SINGLE_SUCCESS;
                                })
                        )
                        .then(literal("lava")
                                .executes((context) -> {
                                    Objects.requireNonNull(getMinecraftClient().getNetworkHandler()).sendChatCommand("minecraft:fill ~12 ~12 ~12 ~-12 ~-12 ~-12 lava");
                                    return SINGLE_SUCCESS;
                                })
                        )
                        .executes((context) -> {
                            Helper.printChatMessage("§4§lError: Incomplete command " + getName() + " fill <block>");
                            return SINGLE_SUCCESS;
                        })
                )
                .then(literal("sphere")
                        .then(literal("air")
                                .executes((context) -> {
                                    Objects.requireNonNull(getMinecraftClient().getNetworkHandler()).sendChatCommand("/sphere air 10");
                                    return SINGLE_SUCCESS;
                                })
                        )
                        .then(literal("lava")
                                .executes((context) -> {
                                    Objects.requireNonNull(getMinecraftClient().getNetworkHandler()).sendChatCommand("/sphere lava 10");
                                    return SINGLE_SUCCESS;
                                })
                        )
                        .executes((context) -> {
                            Helper.printChatMessage("§4§lError: Incomplete command " + getName() + " sphere <block>");
                            return SINGLE_SUCCESS;
                        })
                )
                .executes((context) -> {
                    Helper.printChatMessage("§4§lError: Incomplete command " + getName() + " <method>");
                    return SINGLE_SUCCESS;
                });
    }
}
