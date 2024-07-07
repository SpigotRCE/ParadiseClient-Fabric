package tk.milkthedev.paradiseclientfabric.command.impl;

import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.command.CommandInfo;
import tk.milkthedev.paradiseclientfabric.command.exception.CommandException;
import tk.milkthedev.paradiseclientfabric.exploit.Exploit;
import tk.milkthedev.paradiseclientfabric.exploit.impl.BrigadierExploit;

import java.util.Objects;

@CommandInfo(
        alias = "crash",
        description = "Crashes the server.",
        usage = "crash <method|list|off>"
)
public class CrashCommand extends Command
{
    @Override
    public boolean execute(String commandAlias, String... args) throws CommandException
    {
        if (args.length == 1)
        {
            if (Objects.equals(args[0], "list"))
            {
                Helper.printChatMessage("Available commands:");
                for (Exploit exploit : ParadiseClient_Fabric.getExploitManager().getExploits())
                {
                    Helper.printChatMessage(exploit.getAlias() + " " + exploit.getDescription());
                }
                return true;
            }

            if (Objects.equals(args[0], "off"))
            {
                ParadiseClient_Fabric.getExploitMod().isRunning = false;
                Helper.printChatMessage("[CrashExploit] Stopping all exploits");
                return true;
            }
            ParadiseClient_Fabric.getExploitManager().handleExploit(args[0]);
            return true;
        }
        return false;
    }

    @Override
    public String[] onTabComplete(String commandAlias, String... args)
    {
        return new String[0];
    }
}
