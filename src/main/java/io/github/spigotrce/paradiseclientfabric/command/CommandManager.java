package io.github.spigotrce.paradiseclientfabric.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.command.impl.*;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;

/**
 * Manages and registers commands for the Paradise Client Fabric mod.
 */
public class CommandManager {

    /**
     * The command dispatcher used to register commands.
     */
    public final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();

    /**
     * A list of all registered commands.
     */
    private final ArrayList<Command> commands = new ArrayList<>();


    /**
     * Constructs a new CommandManager instance and registers all commands.
     *
     * @param minecraftClient The Minecraft client instance.
     */
    public CommandManager(MinecraftClient minecraftClient) {
        register(new CopyCommand(minecraftClient));
        register(new CrashCommand(minecraftClient));
        register(new HelpCommand(minecraftClient));
        register(new ForceOPCommand(minecraftClient));
        register(new GriefCommand(minecraftClient));
        register(new ScreenShareCommand(minecraftClient));
        register(new SpamCommand(minecraftClient));
        register(new PlayersCommand(minecraftClient));

        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(
                        new ParadiseCommand(minecraftClient).build()
                )
        );
    }

    /**
     * Registers a command with the command dispatcher.
     *
     * @param command The command to register.
     */
    public void register(Command command) {
        this.commands.add(command);
    }

    /**
     * Returns a list of all registered commands.
     *
     * @return The list of commands.
     */
    public ArrayList<Command> getCommands() {
        return this.commands;
    }

    /**
     * Returns the command with the specified alias, or null if no such command exists.
     *
     * @param alias The alias of the command to find.
     * @return The command with the specified alias, or null if not found.
     */
    public Command getCommand(String alias) {
        for (Command command : commands) {
            if (command.getName().equals(alias))
                return command;
        }
        return null;
    }

    /**
     * This class represents a command the root command to execute all sub commands.
     * It extends the {@link Command} class and overrides the {@link #build()} method to define the command structure.
     *
     * @author SpigotRCE
     * @since 2.28
     */
    private static class ParadiseCommand extends Command {
        public ParadiseCommand(MinecraftClient minecraftClient) {
            super("paradise", "The paradise command!", minecraftClient);
        }

        @Override
        public LiteralArgumentBuilder<FabricClientCommandSource> build() {
            LiteralArgumentBuilder<FabricClientCommandSource> node = literal(getName());
            node.executes(context -> {
                for (Command command : ParadiseClient_Fabric.getCommandManager().getCommands())
                    Helper.printChatMessage("§4§l" + command.getName() + "§r §6" + command.getDescription());
                return SINGLE_SUCCESS;
            });

            ParadiseClient_Fabric.getCommandManager().getCommands().forEach(c -> {
                if (c != this) node.then(c.build());
            });

            return node;
        }
    }
}
