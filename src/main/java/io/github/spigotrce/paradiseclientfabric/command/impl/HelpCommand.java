package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

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
    public LiteralArgumentBuilder<CommandSource> build() {
        LiteralArgumentBuilder<CommandSource> node = literal(getName());

        // Adds sub-commands for each registered command
        ParadiseClient_Fabric.COMMAND_MANAGER.getCommands().forEach(command -> node.then(literal(command.getName()).executes((context) -> {
            Command c = ParadiseClient_Fabric.COMMAND_MANAGER.getCommand(command.getName());
            printDash();
            Helper.printChatMessage("§a" + c.getName() + " §b" + c.getDescription());
            printDash();
            return SINGLE_SUCCESS;
        })));

        // Adds a command to display all registered commands
        node.executes((context -> {
            printDash();
            for (Command command : ParadiseClient_Fabric.COMMAND_MANAGER.getCommands())
                Helper.printChatMessage("§a" + command.getName() + " §b" + command.getDescription());
            printDash();
            Helper.printChatMessage("§aThere are currently §b" + ParadiseClient_Fabric.COMMAND_MANAGER.getCommands().size() + "§a registered commands.");
            printDash();

            return SINGLE_SUCCESS;
        }));

        return node;
    }

    private void printDash() {
        double count = MinecraftClient.getInstance().options.getChatWidth().getValue() * 360 / (MinecraftClient.getInstance().textRenderer.getWidth("-") + 1);
        Helper.printChatMessage("§a" + "-".repeat(((int) count)), false);
    }
}
