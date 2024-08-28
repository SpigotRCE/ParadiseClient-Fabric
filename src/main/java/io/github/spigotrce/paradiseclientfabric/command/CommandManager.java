package io.github.spigotrce.paradiseclientfabric.command;

import com.mojang.brigadier.CommandDispatcher;
import io.github.spigotrce.paradiseclientfabric.command.impl.*;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;

public class CommandManager {

    public final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();
    private final ArrayList<Command> commands = new ArrayList<>();


    public CommandManager() {
        register(new CopyCommand());
        register(new CrashCommand());
        register(new HelpCommand());
        register(new ForceOPCommand());
        register(new GriefCommand());
        register(new ScreenShareCommand());
        register(new SpamCommand());
        register(new PlayersCommand());
        register(new ChatSentryCommand());
        register(new AuthMeVelocityCommand());
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
