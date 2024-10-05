package io.github.spigotrce.paradiseclientfabric;

import io.github.spigotrce.eventbus.event.EventManager;
import io.github.spigotrce.paradiseclientfabric.command.CommandManager;
import io.github.spigotrce.paradiseclientfabric.exploit.ExploitManager;
import io.github.spigotrce.paradiseclientfabric.listener.PacketListener;
import io.github.spigotrce.paradiseclientfabric.mod.*;
import net.fabricmc.api.ModInitializer;
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
    private static final MinecraftClient minecraftClient = MinecraftClient.getInstance();
    /**
     * The instance of {@link EventManager}, which handles the events being fired and listened.
     */
    private static final EventManager eventManager = new EventManager();
    /**
     * The instance of {@link BungeeSpoofMod}, which handles BungeeCord spoofing functionality.
     */
    private static final BungeeSpoofMod bungeeSpoofMod = new BungeeSpoofMod();
    /**
     * The instance of {@link MiscMod}, which handles miscellaneous functionalities.
     */
    private static final MiscMod miscMod = new MiscMod();
    /**
     * The instance of {@link HudMod}, which handles HUD (Heads-Up Display) functionalities.
     */
    private static final HudMod hudMod = new HudMod();
    /**
     * The instance of {@link ChatRoomMod}, which handles chat room functionalities.
     */
    private static final ChatRoomMod chatRoomMod = new ChatRoomMod();
    /**
     * The instance of {@link ExploitMod}, which handles various exploit-related functionalities.
     */
    private static final ExploitMod exploitMod = new ExploitMod();
    /**
     * The instance of {@link CommandManager}, which manages commands in the mod.
     */
    private static final CommandManager commandManager = new CommandManager(getMinecraft());
    /**
     * The instance of {@link ExploitManager}, which manages different types of exploits.
     */
    private static final ExploitManager exploitManager = new ExploitManager(MinecraftClient.getInstance());
    /**
     * The instance of {@link NetworkMod}, which manages network-related functionalities.
     */
    private static final NetworkMod networkMod = new NetworkMod();
    /**
     * The instance of {@link NetworkMod}, which manages network-related functionalities.
     */
    private static final SoundMod soundMod = new SoundMod(MinecraftClient.getInstance());
    /**
     * The instance of {@link MotionBlurMod}, which manages the motion blur.
     */
    private static final MotionBlurMod motionBlurMod = new MotionBlurMod(false, 75);

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
     * Retrieves the instance of {@link SoundMod}.
     *
     * @return The instance of {@link SoundMod}.
     */
    public static SoundMod getSoundMod() {
        return soundMod;
    }

    /**
     * Retrieves the instance of {@link MotionBlurMod}.
     *
     * @return The instance of {@link MotionBlurMod}.
     */
    public static MotionBlurMod getMotionBlurMod() {
        return motionBlurMod;
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
        getCommandManager().init();
        getEventManager().registerListener(new PacketListener());
        getEventManager().registerListener(getCommandManager());
    }
}
