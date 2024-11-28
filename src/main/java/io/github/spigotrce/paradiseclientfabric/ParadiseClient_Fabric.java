package io.github.spigotrce.paradiseclientfabric;

import io.github.spigotrce.eventbus.event.EventManager;
import io.github.spigotrce.paradiseclientfabric.command.CommandManager;
import io.github.spigotrce.paradiseclientfabric.exploit.ExploitManager;
import io.github.spigotrce.paradiseclientfabric.listener.ChannelListener;
import io.github.spigotrce.paradiseclientfabric.listener.PacketListener;
import io.github.spigotrce.paradiseclientfabric.mod.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.Objects;

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
    private static final MinecraftClient minecraftClient = MinecraftClient.getInstance();
    /**
     * The instance of {@link EventManager}, which handles the events being fired and listened.
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
     * The instance of {@link Thread}, for update checking
     */
    private static Thread updateCheckerThread;

    /**
     * Retrieves the instance of {@link EventManager}.
     *
     * @return The instance of {@link EventManager}.
     */
    public static EventManager getEventManager() {
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
     * Retrieves the instance of {@link Thread}.
     *
     * @return The instance of {@link Thread}.
     */
    public static Thread getUpdateCheckerThread() {
        return updateCheckerThread;
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

    /**
     * Retrieves the instance of {@link MinecraftClient}.
     *
     * @return The instance of {@link MinecraftClient}.
     */
    public static MinecraftClient getMinecraft() {
        return minecraftClient;
    }

    @Override
    public void onInitialize() {
        KeyBinding paradiseCommandOpener =  KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Open paradise command",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_COMMA,
                        Constants.MOD_NAME
                )
        );
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (paradiseCommandOpener.wasPressed())
                MinecraftClient.getInstance().setScreen(new ChatScreen(getCommandManager().prefix));
        });

        updateCheckerThread = new Thread(() -> {
            try {
                String latestVersion = Helper.getLatestReleaseTag();
                if (latestVersion == null) return;
                getMiscMod().latestVersion = Helper.getLatestReleaseTag();
                if (!Objects.equals(getMiscMod().latestVersion, Constants.VERSION))
                    getMiscMod().isClientOutdated = true;
            } catch (IOException e) {
                Constants.LOGGER.error("Error getting latest release tag", e);
            }
        });
        updateCheckerThread.start();
    }

    public static void onClientInitialize() {
        initializeMods();
        initializeManagers();
        initializeListeners();
    }

    private static void initializeMods() {
        bungeeSpoofMod = new BungeeSpoofMod();
        miscMod = new MiscMod();
        hudMod = new HudMod();
        chatRoomMod = new ChatRoomMod();
        exploitMod = new ExploitMod();
        networkMod = new NetworkMod();
    }

    private static void initializeManagers() {
        eventManager = new EventManager();
        exploitManager = new ExploitManager(getMinecraft());
        getExploitManager().init();
        commandManager = new CommandManager(getMinecraft());
        getCommandManager().init();
    }

    private static void initializeListeners() {
        getEventManager().registerListener(new PacketListener());
        getEventManager().registerListener(getCommandManager());
        getEventManager().registerListener(new ChannelListener());
    }
}
