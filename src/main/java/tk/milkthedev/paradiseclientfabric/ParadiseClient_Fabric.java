package tk.milkthedev.paradiseclientfabric;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.command.CommandManager;
import tk.milkthedev.paradiseclientfabric.command.impl.SayCommand;

public class ParadiseClient_Fabric implements ModInitializer
{
    private static BungeeSpoofMod bungeeSpoofMod;
    private static MiscMod miscMod;
    private static CommandManager commandManager;

    public static BungeeSpoofMod getBungeeSpoofMod() {return bungeeSpoofMod;}
    public static MiscMod getMiscMod() {return miscMod;}
    public static CommandManager getCommandManager() {return commandManager;}

    @Override
    public void onInitialize()
    {
        bungeeSpoofMod = new BungeeSpoofMod();
        miscMod = new MiscMod();
        commandManager = new CommandManager(
                new SayCommand()
        );
    }
}
