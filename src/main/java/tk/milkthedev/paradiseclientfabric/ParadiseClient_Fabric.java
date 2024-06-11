package tk.milkthedev.paradiseclientfabric;

import net.fabricmc.api.ModInitializer;
import tk.milkthedev.paradiseclientfabric.command.CommandManager;
import tk.milkthedev.paradiseclientfabric.command.impl.GriefCommand;
import tk.milkthedev.paradiseclientfabric.command.impl.HelpCommand;
import tk.milkthedev.paradiseclientfabric.command.impl.SayCommand;
import tk.milkthedev.paradiseclientfabric.command.impl.ScreenShareCommand;
import tk.milkthedev.paradiseclientfabric.mod.BungeeSpoofMod;
import tk.milkthedev.paradiseclientfabric.mod.HudMod;
import tk.milkthedev.paradiseclientfabric.mod.MiscMod;

public class ParadiseClient_Fabric implements ModInitializer
{
    private static BungeeSpoofMod bungeeSpoofMod;
    private static MiscMod miscMod;
    private static HudMod hudMod;
    private static CommandManager commandManager;

    public static BungeeSpoofMod getBungeeSpoofMod() {return bungeeSpoofMod;}
    public static MiscMod getMiscMod() {return miscMod;}
    public static HudMod getHudMod() {return hudMod;}
    public static CommandManager getCommandManager() {return commandManager;}

    @Override
    public void onInitialize()
    {
        bungeeSpoofMod = new BungeeSpoofMod();
        miscMod = new MiscMod();
        hudMod = new HudMod();
        commandManager = new CommandManager(
                new SayCommand(),
                new ScreenShareCommand(),
                new GriefCommand(),
                new HelpCommand()
        );
    }
}
