package tk.milkthedev.paradiseclientfabric.command.impl;

import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.command.CommandInfo;
import tk.milkthedev.paradiseclientfabric.command.exception.CommandException;
import tk.milkthedev.paradiseclientfabric.exploit.CompletionCrashExploit;

@CommandInfo(
        alias = "crash",
        description = "Crashes the server. As of now, crashes through the StackOverFlow crash exploit",
        usage = "crash <method|list>"
)
public class CrashCommand extends Command
{
    @Override
    public boolean execute(String commandAlias, String... args) throws CommandException
    {
        new CompletionCrashExploit().enable();
        return true;
    }

    @Override
    public String[] onTabComplete(String commandAlias, String... args)
    {
        return new String[0];
    }
}
