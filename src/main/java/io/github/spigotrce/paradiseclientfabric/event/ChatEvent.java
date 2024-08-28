package io.github.spigotrce.paradiseclientfabric.event;

import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.mod.MiscMod;

public class ChatEvent {
    private static final MiscMod miscMod = ParadiseClient_Fabric.getMiscMod();

    public static boolean outgoingChatMessage(String message) {
        if (message.startsWith("/")) {
            return true;
        } else {
            miscMod.lastMessage = message;
        }
        return true; // Don't cancel the event
    }
}
