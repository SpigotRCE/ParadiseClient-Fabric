package io.github.spigotrce.paradiseclientfabric.event;

import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.mod.MiscMod;

/**
 * This class handles chat-related events.
 *  @author SpigotRCE
 * @since 1.2
 */
public class ChatEvent {
    /**
     * A reference to the {@link MiscMod} instance.
     */
    private static final MiscMod miscMod = ParadiseClient_Fabric.getMiscMod();

    /**
     * Handles outgoing chat messages.
     *
     * @param message The outgoing chat message.
     * @return {@code true} if the event should proceed, {@code false} otherwise.
     *         In this case, the event always proceeds, so it returns {@code true}.
     *         However, it also updates the {@link MiscMod#lastMessage} field with the outgoing message if it doesn't start with "/".
     */
    public static boolean outgoingChatMessage(String message) {
        if (message.startsWith("/")) {
            return true;
        } else {
            miscMod.lastMessage = message;
        }
        return true; // Don't cancel the event
    }
}
