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
    public static final MinecraftClient minecraftClient = MinecraftClient.getInstance();
    /**
     * The instance of {@link EventManager}, which handles the events being fired and listened.
     */
    public static EventManager eventManager;
    /**
     * The instance of {@link BungeeSpoofMod}, which handles BungeeCord spoofing functionality.
     */
    public static BungeeSpoofMod bungeeSpoofMod;
    /**
     * The instance of {@link MiscMod}, which handles miscellaneous functionalities.
     */
    public static MiscMod miscMod;
    /**
     * The instance of {@link HudMod}, which handles HUD (Heads-Up Display) functionalities.
     */
    public static HudMod hudMod;
    /**
     * The instance of {@link ChatRoomMod}, which handles chat room functionalities.
     */
    public static ChatRoomMod chatRoomMod;
    /**
     * The instance of {@link ExploitMod}, which handles various exploit-related functionalities.
     */
    public static ExploitMod exploitMod;
    /**
     * The instance of {@link CommandManager}, which manages commands in the mod.
     */
    public static CommandManager commandManager;
    /**
     * The instance of {@link ExploitManager}, which manages different types of exploits.
     */
    public static ExploitManager exploitManager;
    /**
     * The instance of {@link NetworkMod}, which manages network-related functionalities.
     */
    public static NetworkMod networkMod;

    public static void init() {
        // Ajoute un listener pour vider les channels à la déconnexion
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ChannelListener.clearChannels();
        });
    }

    public static void onClientInitialize() {
        initializeMods();
        initializeManagers();
        initializeListeners();

        new Thread(() -> {
            try {
                String latestVersion = Helper.getLatestReleaseTag();
                if (latestVersion == null) return;
                ParadiseClient_Fabric.miscMod.latestVersion = latestVersion;
                if (!Objects.equals(ParadiseClient_Fabric.miscMod.latestVersion, Constants.VERSION))
                    ParadiseClient_Fabric.miscMod.isClientOutdated = true;

                Constants.WINDOW_TITLE = Constants.MOD_NAME + " [" + Constants.EDITION + "] " + Constants.VERSION + " " +
                        (ParadiseClient_Fabric.miscMod.isClientOutdated ? "Outdated" : "");
            } catch (IOException e) {
                Constants.LOGGER.error("Error getting latest release tag", e);
            }
        }).start();
    }

    public static void initializeMods() {
        bungeeSpoofMod = new BungeeSpoofMod();
        miscMod = new MiscMod();
        hudMod = new HudMod();
        chatRoomMod = new ChatRoomMod();
        exploitMod = new ExploitMod();
        networkMod = new NetworkMod();
    }

    public static void initializeManagers() {
        eventManager = new EventManager();
        exploitManager = new ExploitManager(ParadiseClient_Fabric.minecraftClient);
        ParadiseClient_Fabric.exploitManager.init();
        commandManager = new CommandManager(ParadiseClient_Fabric.minecraftClient);
        ParadiseClient_Fabric.commandManager.init();
    }

    public static void initializeListeners() {
        ParadiseClient_Fabric.eventManager.registerListener(new PacketListener());
        ParadiseClient_Fabric.eventManager.registerListener(ParadiseClient_Fabric.commandManager);
        ParadiseClient_Fabric.eventManager.registerListener(new ChannelListener());
    }

    @Override
    public void onInitialize() {
        KeyBinding paradiseCommandOpener = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Open paradise command",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_COMMA,
                        Constants.MOD_NAME
                )
        );
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (paradiseCommandOpener.wasPressed())
                MinecraftClient.getInstance().setScreen(new ChatScreen(ParadiseClient_Fabric.commandManager.prefix));
        });
    }
}
