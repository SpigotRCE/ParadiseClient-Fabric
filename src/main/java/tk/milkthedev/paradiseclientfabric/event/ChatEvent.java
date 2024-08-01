package tk.milkthedev.paradiseclientfabric.event;

import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;
import tk.milkthedev.paradiseclientfabric.command.CommandManager;
import tk.milkthedev.paradiseclientfabric.mod.MiscMod;

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
