package tk.milkthedev.paradiseclientfabric;

import net.fabricmc.api.ModInitializer;
import tk.milkthedev.paradiseclientfabric.command.CommandManager;
import tk.milkthedev.paradiseclientfabric.exploit.ExploitManager;
import tk.milkthedev.paradiseclientfabric.exploit.impl.*;
import tk.milkthedev.paradiseclientfabric.mod.*;

public class ParadiseClient_Fabric implements ModInitializer {
    private static BungeeSpoofMod bungeeSpoofMod;
    private static MiscMod miscMod;
    private static HudMod hudMod;
    private static ChatRoomMod chatRoomMod;
    private static ExploitMod exploitMod;
    private static CommandManager commandManager;
    private static ExploitManager exploitManager;
    private static NetworkMod networkMod;


    public static BungeeSpoofMod getBungeeSpoofMod() {
        return bungeeSpoofMod;
    }

    public static MiscMod getMiscMod() {
        return miscMod;
    }

    public static HudMod getHudMod() {
        return hudMod;
    }

    public static ChatRoomMod getChatRoomMod() {
        return chatRoomMod;
    }

    public static ExploitMod getExploitMod() {
        return exploitMod;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static ExploitManager getExploitManager() {
        return exploitManager;
    }

    public static NetworkMod getNetworkMod() {
        return networkMod;
    }

    @Override
    public void onInitialize() {
        bungeeSpoofMod = new BungeeSpoofMod();
        miscMod = new MiscMod();
        hudMod = new HudMod();
        chatRoomMod = new ChatRoomMod();
        exploitMod = new ExploitMod();
        commandManager = new CommandManager();
        networkMod = new NetworkMod();
        exploitManager = new ExploitManager(
                new BrigadierExploit(),
                new PaperWindowExploit(),
                new SignExploit(),
                new NegativeInfinityExploit(),
                new SlotCrashExploit()
        );
    }
}
