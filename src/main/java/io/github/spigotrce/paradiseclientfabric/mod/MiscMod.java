package io.github.spigotrce.paradiseclientfabric.mod;

import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;

/**
 * Manages miscellaneous data related to network packets and screen state.
 * <p>
 * This class keeps track of various network packet details and the current screen being displayed.
 * </p>
 *
 * @author SpigotRCE
 * @since 1.1
 */
public class MiscMod {
    /**
     * Message which needs to be delayed before sending.
     */
    public final ArrayList<String> delayedMessages = new ArrayList<>();
    /**
     * The last message sent or received.
     */
    public String lastMessage;
    /**
     * The current screen being displayed in the client.
     */
    public Screen currentScreen;
}
