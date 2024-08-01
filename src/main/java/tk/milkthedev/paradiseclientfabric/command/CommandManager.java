package tk.milkthedev.paradiseclientfabric.command;

import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import tk.milkthedev.paradiseclientfabric.command.impl.*;

import java.util.ArrayList;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

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
