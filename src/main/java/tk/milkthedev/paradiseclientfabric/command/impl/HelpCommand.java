package tk.milkthedev.paradiseclientfabric.command.impl;

import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.command.CommandInfo;
import tk.milkthedev.paradiseclientfabric.command.exception.CommandException;

import java.util.ArrayList;
import java.util.Arrays;

@CommandInfo(
        alias = "help",
        description = "Displays this message",
        usage = "help [command]"
)
public class HelpCommand extends Command
{
    @Override
    public boolean execute(String commandAlias, String... args) throws CommandException
    {
        if (args.length == 0)
        {
            Helper.printChatMessage("Available commands:");
            for (Command command : ParadiseClient_Fabric.getCommandManager().getCommands()) {Helper.printChatMessage(command.getUsage() + " " + command.getDescription());}
            return true;
        }

        Command command = ParadiseClient_Fabric.getCommandManager().getCommand(args[0]);
        if (command == null) {throw new CommandException("Unknown command!");}
        Helper.printChatMessage(command.getUsage() + " " + command.getDescription());
        return true;
    }

    @Override
    public String[] onTabComplete(String commandAlias, String... args)
    {
        if (args.length > 0) {return new String[0];}
        ArrayList<String> suggestions = new ArrayList<>();
        for (Command command : ParadiseClient_Fabric.getCommandManager().getCommands()) {suggestions.add(command.getAlias());}
        return suggestions.toArray(new String[0]);
    }
}
