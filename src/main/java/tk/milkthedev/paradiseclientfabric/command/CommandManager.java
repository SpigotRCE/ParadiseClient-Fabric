package tk.milkthedev.paradiseclientfabric.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import net.minecraft.command.CommandSource;
import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.command.exception.CommandException;

import java.util.Arrays;
import java.util.List;

public class CommandManager {

    public final List<Command> commands;

    public CommandManager(Command... commands) {this.commands = Arrays.asList(commands);}

    public String getPrefix() {return "?";}

    public void handleCommand(String input)
    {
        String[] args = input.split(" ");
        String alias = args[0].replace(getPrefix(), "");
        try
        {
            Command command = getCommand(alias);
            if (command == null) {throw new CommandException("Unknown command! Use " + getPrefix() + "help to see the list of commands available.");}

            if (!command.execute(alias, Arrays.copyOfRange(args, 1, args.length))) { Helper.printChatMessage("Usage: " + command.getUsage());}
        } catch (CommandException e)
        {
            Helper.printChatMessage(e.getMessage());
        }
    }

    public CommandDispatcher<CommandSource> getCommandDispatcher(StringReader stringReader, CommandDispatcher<CommandSource> dispatcher)
    {
        String input = stringReader.getString();

        if (getPrefix().equals(input))
        {
            for (Command command : getCommands()) {dispatcher.register(literal(command.getAlias()));}
            return dispatcher;
        }

        String[] args = Arrays.copyOfRange(input.split(" "), 1, input.split(" ").length);
        String alias = input.split(" ")[0].replace(getPrefix(), "");
        Command command = getCommand(alias);

        if (command == null) return dispatcher;

        String[] suggestions = command.onTabComplete(alias, args);

        CommandNode<CommandSource> node = dispatcher.getRoot();

        node = node.getChild(alias);
        for (String arg : args)
            if (node.getChild(arg) != null)
                node = node.getChild(arg);

        node.getChildren().removeAll(node.getChildren());
        for (String suggestion : suggestions)
            node.addChild(literal(suggestion).build());

        return dispatcher;
    }


    public void registerCommand(Command command) {this.commands.add(command);}

    public void registerCommands(Command... commands) {this.commands.addAll(Arrays.asList(commands));}

    public void unregisterCommand(Command command) {this.commands.remove(command);}

    public void unregisterCommands(Command... commands) {this.commands.removeAll(Arrays.asList(commands));}

    public Command getCommand(String alias)
    {
        for (Command command : commands)
        {
            if (command.getAlias().equals(alias)) {return command;}
        }
        return null;
    }

    public List<Command> getCommands() {return commands;}

    public LiteralArgumentBuilder<CommandSource> literal(final String name) {return LiteralArgumentBuilder.literal(name);}
}