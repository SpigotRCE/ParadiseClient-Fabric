package io.github.spigotrce.paradiseclientfabric.command;

import com.mojang.brigadier.CommandDispatcher;
import io.github.spigotrce.paradiseclientfabric.command.impl.*;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;

public class CommandManager {

    public final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();
    private final ArrayList<Command> commands = new ArrayList<>();


    public CommandManager(MinecraftClient minecraftClient) {
        register(new CopyCommand(minecraftClient));
        register(new CrashCommand(minecraftClient));
        register(new HelpCommand(minecraftClient));
        register(new ForceOPCommand(minecraftClient));
        register(new GriefCommand(minecraftClient));
        register(new ScreenShareCommand(minecraftClient));
        register(new SpamCommand(minecraftClient));
        register(new PlayersCommand(minecraftClient));
    }

    private void register(Command command) {
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(
                        command.build()
                )
        );
        this.commands.add(command);
    }

    public ArrayList<Command> getCommands() {
        return this.commands;
    }

    public Command getCommand(String alias) {
        for (Command command : commands) {
            if (command.getName().equals(alias))
                return command;

        }
        return null;
    }
}
