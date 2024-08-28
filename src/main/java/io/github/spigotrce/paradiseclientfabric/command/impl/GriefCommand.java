package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.command.Command;

import java.util.Objects;

public class GriefCommand extends Command {
    public GriefCommand() {
        super("paradisegrief", "Multiple grief commands");
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return literal(getName())
                .then(literal("tpall")
                        .executes((context) -> {
                            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("tpall");
                            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("etpall");
                            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("minecraft:tp @a @p");
                            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("tp @a @p");
                            return SINGLE_SUCCESS;
                        })
                )
                .then(literal("fill")
                        .then(literal("air")
                                .executes((context) -> {
                                    Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("minecraft:fill ~12 ~12 ~12 ~-12 ~-12 ~-12 air");
                                    return SINGLE_SUCCESS;
                                })
                        )
                        .then(literal("lava")
                                .executes((context) -> {
                                    Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("minecraft:fill ~12 ~12 ~12 ~-12 ~-12 ~-12 lava");
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
                                    Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("/sphere air 10");
                                    return SINGLE_SUCCESS;
                                })
                        )
                        .then(literal("lava")
                                .executes((context) -> {
                                    Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("/sphere lava 10");
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
