package io.github.spigotrce.paradiseclientfabric;

import io.github.spigotrce.eventbus.event.EventManager;
import io.github.spigotrce.paradiseclientfabric.command.CommandManager;
import io.github.spigotrce.paradiseclientfabric.exploit.ExploitManager;
import io.github.spigotrce.paradiseclientfabric.hook.viafabric.SelectedProtocolVersion;
import io.github.spigotrce.paradiseclientfabric.listener.ChannelListener;
import io.github.spigotrce.paradiseclientfabric.listener.PacketListener;
import io.github.spigotrce.paradiseclientfabric.mod.*;
import io.github.spigotrce.paradiseclientfabric.packet.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
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
public class ParadiseClient_Fabric implements ModInitializer, ClientModInitializer {
    /**
     * The Minecraft client instance.
     */
    public static final MinecraftClient MINECRAFT_CLIENT = MinecraftClient.getInstance();
    /**
     * The instance of {@link EventManager}, which handles the events being fired and listened.
     */
    public static EventManager EVENT_MANAGER;
    /**
     * The instance of {@link BungeeSpoofMod}, which handles BungeeCord spoofing functionality.
     */
    public static BungeeSpoofMod BUNGEE_SPOOF_MOD;
    /**
     * The instance of {@link MiscMod}, which handles miscellaneous functionalities.
     */
    public static MiscMod MISC_MOD;
    /**
     * The instance of {@link HudMod}, which handles HUD (Heads-Up Display) functionalities.
     */
    public static HudMod HUD_MOD;
    /**
     * The instance of {@link ChatRoomMod}, which handles chat room functionalities.
     */
    public static ChatRoomMod CHAT_ROOM_MOD;
    /**
     * The instance of {@link ExploitMod}, which handles various exploit-related functionalities.
     */
    public static ExploitMod EXPLOIT_MOD;
    /**
     * The instance of {@link CommandManager}, which manages commands in the mod.
     */
    public static CommandManager COMMAND_MANAGER;
    /**
     * The instance of {@link ExploitManager}, which manages different types of exploits.
     */
    public static ExploitManager EXPLOIT_MANAGER;
    /**
     * The instance of {@link NetworkMod}, which manages network-related functionalities.
     */
    public static NetworkMod NETWORK_MOD;
    /**
     * The instance of {@link SelectedProtocolVersion}, which stores the protocol version selected in VFP.
     */
    public static SelectedProtocolVersion SELECTED_PROTOCOL_VERSION = new SelectedProtocolVersion();

    public static void onClientInitialize() {
        registerChannels();
        initializeMods();
        initializeManagers();
        initializeListeners();

        new Thread(() -> {
            try {
                String latestVersion = Helper.getLatestReleaseTag();
                if (latestVersion == null) return;
                ParadiseClient_Fabric.MISC_MOD.latestVersion = latestVersion;
                if (!Objects.equals(ParadiseClient_Fabric.MISC_MOD.latestVersion, Constants.VERSION))
                    ParadiseClient_Fabric.MISC_MOD.isClientOutdated = true;

                Constants.reloadTitle();
            } catch (IOException e) {
                Constants.LOGGER.error("Error getting latest release tag", e);
            }
        }).start();
    }

    public static void registerChannels() {
        PayloadTypeRegistry.playC2S().register(VelocityReportPayloadPacket.ID, VelocityReportPayloadPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(PurpurExploitPayloadPacket.ID, PurpurExploitPayloadPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(AuthMeVelocityPayloadPacket.ID, AuthMeVelocityPayloadPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(ChatSentryPayloadPacket.ID, ChatSentryPayloadPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(ECBPayloadPacket.ID, ECBPayloadPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SignedVelocityPayloadPacket.ID, SignedVelocityPayloadPacket.CODEC);
    }

    public static void initializeMods() {
        BUNGEE_SPOOF_MOD = new BungeeSpoofMod();
        MISC_MOD = new MiscMod();
        HUD_MOD = new HudMod();
        CHAT_ROOM_MOD = new ChatRoomMod();
        EXPLOIT_MOD = new ExploitMod();
        NETWORK_MOD = new NetworkMod();
    }

    public static void initializeManagers() {
        EVENT_MANAGER = new EventManager();
        EXPLOIT_MANAGER = new ExploitManager(ParadiseClient_Fabric.MINECRAFT_CLIENT);
        ParadiseClient_Fabric.EXPLOIT_MANAGER.init();
        COMMAND_MANAGER = new CommandManager(ParadiseClient_Fabric.MINECRAFT_CLIENT);
        ParadiseClient_Fabric.COMMAND_MANAGER.init();
    }

    public static void initializeListeners() {
        ParadiseClient_Fabric.EVENT_MANAGER.registerListener(new PacketListener());
        ParadiseClient_Fabric.EVENT_MANAGER.registerListener(ParadiseClient_Fabric.COMMAND_MANAGER);
        ParadiseClient_Fabric.EVENT_MANAGER.registerListener(new ChannelListener());
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
                MinecraftClient.getInstance().setScreen(new ChatScreen(ParadiseClient_Fabric.COMMAND_MANAGER.prefix));
        });
    }

    @Override
    public void onInitializeClient() {
        onClientInitialize();
    }
}
