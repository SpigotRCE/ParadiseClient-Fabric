package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

/**
 * Represents a command for crashing the server using various exploits.
 *
 * @author SpigotRCE
 * @since 1.0.0
 */
public class CrashCommand extends Command {

    /**
     * Constructs a new instance of CrashCommand.
     *
     * @param minecraftClient The Minecraft client instance.
     */
    public CrashCommand(MinecraftClient minecraftClient) {
        super("crash", "Crashes the server", minecraftClient);
    }

    /**
     * Builds the command structure using Brigadier's LiteralArgumentBuilder.
     *
     * @return The root node of the command structure.
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        LiteralArgumentBuilder<FabricClientCommandSource> node = literal(getName());

        // Add subcommands for each exploit
        ParadiseClient_Fabric.getExploitManager().getExploits().forEach(exploit -> {
            node.then(literal(exploit.getAlias())
                    .executes((context) -> {
                        ParadiseClient_Fabric.getExploitManager().handleExploit(exploit.getAlias());
                        return SINGLE_SUCCESS;
                    }));
        });

        // Add a subcommand to stop all exploits
        node.then(literal("off")
                .executes((context) -> {
                    ParadiseClient_Fabric.getExploitMod().isRunning = false;
                    Helper.printChatMessage("[CrashExploit] Stopping all exploits");
                    return SINGLE_SUCCESS;
                }));

        return node;
    }
}
