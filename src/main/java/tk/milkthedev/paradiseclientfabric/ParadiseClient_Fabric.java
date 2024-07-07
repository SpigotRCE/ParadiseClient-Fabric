package tk.milkthedev.paradiseclientfabric;

import net.fabricmc.api.ModInitializer;
import tk.milkthedev.paradiseclientfabric.command.CommandManager;
import tk.milkthedev.paradiseclientfabric.command.impl.*;
import tk.milkthedev.paradiseclientfabric.exploit.ExploitManager;
import tk.milkthedev.paradiseclientfabric.exploit.impl.BrigadierExploit;
import tk.milkthedev.paradiseclientfabric.exploit.impl.PaperWindowExploit;
import tk.milkthedev.paradiseclientfabric.mod.*;

public class ParadiseClient_Fabric implements ModInitializer
{
    private static BungeeSpoofMod bungeeSpoofMod;
    private static MiscMod miscMod;
    private static HudMod hudMod;
    private static ChatRoomMod chatRoomMod;
    private static ExploitMod exploitMod;
    private static CommandManager commandManager;
    private static ExploitManager exploitManager;


    public static BungeeSpoofMod getBungeeSpoofMod() {return bungeeSpoofMod;}
    public static MiscMod getMiscMod() {return miscMod;}
    public static HudMod getHudMod() {return hudMod;}
    public static ChatRoomMod getChatRoomMod() {return chatRoomMod;}
    public static ExploitMod getExploitMod() {return exploitMod;}
    public static CommandManager getCommandManager() {return commandManager;}
    public static ExploitManager getExploitManager() {return exploitManager;}


    @Override
    public void onInitialize()
    {
        bungeeSpoofMod = new BungeeSpoofMod();
        miscMod = new MiscMod();
        hudMod = new HudMod();
        chatRoomMod = new ChatRoomMod();
        exploitMod = new ExploitMod();
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
