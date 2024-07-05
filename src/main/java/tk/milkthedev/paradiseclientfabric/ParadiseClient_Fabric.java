package tk.milkthedev.paradiseclientfabric;

import net.fabricmc.api.ModInitializer;
import tk.milkthedev.paradiseclientfabric.command.CommandManager;
import tk.milkthedev.paradiseclientfabric.command.impl.*;
import tk.milkthedev.paradiseclientfabric.exploit.ExploitManager;
import tk.milkthedev.paradiseclientfabric.exploit.impl.BrigadierExploit;
import tk.milkthedev.paradiseclientfabric.exploit.impl.PaperWindowExploit;
import tk.milkthedev.paradiseclientfabric.mod.BungeeSpoofMod;
import tk.milkthedev.paradiseclientfabric.mod.ChatRoomMod;
import tk.milkthedev.paradiseclientfabric.mod.HudMod;
import tk.milkthedev.paradiseclientfabric.mod.MiscMod;

public class ParadiseClient_Fabric implements ModInitializer
{
    private static BungeeSpoofMod bungeeSpoofMod;
    private static MiscMod miscMod;
    private static HudMod hudMod;
    private static CommandManager commandManager;
    private static ExploitManager exploitManager;
    private static ChatRoomMod chatRoomMod;

    public static BungeeSpoofMod getBungeeSpoofMod() {return bungeeSpoofMod;}
    public static MiscMod getMiscMod() {return miscMod;}
    public static HudMod getHudMod() {return hudMod;}
    public static CommandManager getCommandManager() {return commandManager;}
    public static ExploitManager getExploitManager() {return exploitManager;}
    public static ChatRoomMod getChatRoomMod() {return chatRoomMod;}

    @Override
    public void onInitialize()
    {
        bungeeSpoofMod = new BungeeSpoofMod();
        miscMod = new MiscMod();
        hudMod = new HudMod();
        chatRoomMod = new ChatRoomMod();
        commandManager = new CommandManager(
                new SayCommand(),
                new ScreenShareCommand(),
                new GriefCommand(),
                new HelpCommand(),
                new ChatRoomCommand(),
                new SpamCommand(),
                new CrashCommand(),
                new ForceOPCommand(),
                new CopyCommand()
        );
        exploitManager = new ExploitManager(
                new BrigadierExploit(),
                new PaperWindowExploit()
        );
    }
}
