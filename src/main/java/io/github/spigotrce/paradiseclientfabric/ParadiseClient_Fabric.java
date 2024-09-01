package io.github.spigotrce.paradiseclientfabric;

import io.github.spigotrce.eventbus.event.EventManager;
import io.github.spigotrce.paradiseclientfabric.exploit.impl.*;
import io.github.spigotrce.paradiseclientfabric.mod.*;
import net.fabricmc.api.ModInitializer;
import io.github.spigotrce.paradiseclientfabric.command.CommandManager;
import io.github.spigotrce.paradiseclientfabric.exploit.ExploitManager;
import net.minecraft.client.MinecraftClient;

/**
 * The main class for the ParadiseClient Fabric mod.
 * <p>
 * This class implements the {@link ModInitializer} interface and is responsible for initializing
 * the various components and modules of the mod when the mod is loaded.
 * </p>
 *
 * @author SpigotRCE
 * @since 1.0
 */
public class ParadiseClient_Fabric implements ModInitializer {
    /**
     * The Minecraft client instance.
     */
    private MinecraftClient minecraftClient;

    /**
     * The instance of {@link EventManager}, which handles the events being fired and liste
     */
    private static EventManager eventManager;

    /**
     * The instance of {@link BungeeSpoofMod}, which handles BungeeCord spoofing functionality.
     */
    private static BungeeSpoofMod bungeeSpoofMod;

    /**
     * The instance of {@link MiscMod}, which handles miscellaneous functionalities.
     */
    private static MiscMod miscMod;

    /**
     * The instance of {@link HudMod}, which handles HUD (Heads-Up Display) functionalities.
     */
    private static HudMod hudMod;

    /**
     * The instance of {@link ChatRoomMod}, which handles chat room functionalities.
     */
    private static ChatRoomMod chatRoomMod;

    /**
     * The instance of {@link ExploitMod}, which handles various exploit-related functionalities.
     */
    private static ExploitMod exploitMod;

    /**
     * The instance of {@link CommandManager}, which manages commands in the mod.
     */
    private static CommandManager commandManager;

    /**
     * The instance of {@link ExploitManager}, which manages different types of exploits.
     */
    private static ExploitManager exploitManager;

    /**
     * The instance of {@link NetworkMod}, which manages network-related functionalities.
     */
    private static NetworkMod networkMod;

    /**
     * Retrieves the instance of {@link EventManager}.
     *
     * @return The instance of {@link EventManager}.
     */
    public static EventManager getEvenManager() {
        return eventManager;
    }

    /**
     * Retrieves the instance of {@link BungeeSpoofMod}.
     *
     * @return The instance of {@link BungeeSpoofMod}.
     */
    public static BungeeSpoofMod getBungeeSpoofMod() {
        return bungeeSpoofMod;
    }

    /**
     * Retrieves the instance of {@link MiscMod}.
     *
     * @return The instance of {@link MiscMod}.
     */
    public static MiscMod getMiscMod() {
        return miscMod;
    }

    /**
     * Retrieves the instance of {@link HudMod}.
     *
     * @return The instance of {@link HudMod}.
     */
    public static HudMod getHudMod() {
        return hudMod;
    }

    /**
     * Retrieves the instance of {@link ChatRoomMod}.
     *
     * @return The instance of {@link ChatRoomMod}.
     */
    public static ChatRoomMod getChatRoomMod() {
        return chatRoomMod;
    }

    /**
     * Retrieves the instance of {@link ExploitMod}.
     *
     * @return The instance of {@link ExploitMod}.
     */
    public static ExploitMod getExploitMod() {
        return exploitMod;
    }

    /**
     * Retrieves the instance of {@link CommandManager}.
     *
     * @return The instance of {@link CommandManager}.
     */
    public static CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Retrieves the instance of {@link ExploitManager}.
     *
     * @return The instance of {@link ExploitManager}.
     */
    public static ExploitManager getExploitManager() {
        return exploitManager;
    }

    /**
     * Retrieves the instance of {@link NetworkMod}.
     *
     * @return The instance of {@link NetworkMod}.
     */
    public static NetworkMod getNetworkMod() {
        return networkMod;
    }

    @Override
    public void onInitialize() {
        this.minecraftClient = MinecraftClient.getInstance();
        eventManager = new EventManager();
        bungeeSpoofMod = new BungeeSpoofMod();
        miscMod = new MiscMod();
        hudMod = new HudMod();
        chatRoomMod = new ChatRoomMod();
        exploitMod = new ExploitMod();
        commandManager = new CommandManager(this.minecraftClient);
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
