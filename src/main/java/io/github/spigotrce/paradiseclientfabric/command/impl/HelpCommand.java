package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.minecraft.client.MinecraftClient;

/**
 * Represents a command that displays help information for other commands.
 *
 * @author SpigotRCE
 * @since 1.4
 */
public class HelpCommand extends Command {

    /**
     * Constructs a new instance of {@link HelpCommand}.
     *
     * @param minecraftClient The Minecraft client instance.
     */
    public HelpCommand(MinecraftClient minecraftClient) {
        super("help", "Shows help page", minecraftClient);
    }

    /**
     * Builds the command structure using Brigadier's {@link LiteralArgumentBuilder}.
     *
     * @return The built command structure.
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        LiteralArgumentBuilder<FabricClientCommandSource> node = literal(getName());

        // Adds sub-commands for each registered command
        for (Command command : ParadiseClient_Fabric.getCommandManager().getCommands())
            node.then(literal(command.getName()).executes((context) -> {
                Command c = ParadiseClient_Fabric.getCommandManager().getCommand(context.getInput().split(" ")[1]);
                Helper.printChatMessage("§4§l" + c.getName() + "§r §6" + c.getDescription());
                return SINGLE_SUCCESS;
            }));

        // Adds a command to display all registered commands
        node.executes((context -> {
            for (Command command : ParadiseClient_Fabric.getCommandManager().getCommands())
                Helper.printChatMessage("§4§l" + command.getName() + "§r §6" + command.getDescription());
            return SINGLE_SUCCESS;
        }));

        return node;
    }
}
