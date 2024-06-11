package tk.milkthedev.paradiseclientfabric.command.impl;

import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.command.exception.CommandException;
import tk.milkthedev.paradiseclientfabric.command.CommandInfo;

@CommandInfo(
        alias = "screenshare",
        description = "Disables/Enables server IP in HUD",
        usage = "screenshare []"
)
public class ScreenShareCommand extends Command
{
    @Override
    public boolean execute(String commandAlias, String... args) throws CommandException
    {
        ParadiseClient_Fabric.getHudMod().showServerIP = !ParadiseClient_Fabric.getHudMod().showServerIP;
        Helper.printChatMessage(ParadiseClient_Fabric.getHudMod().showServerIP ? "Server IP shown" : "Server IP hidden");
        return true;
    }

    @Override
    public String[] onTabComplete(String commandAlias, String... args) {return new String[0];}
}
