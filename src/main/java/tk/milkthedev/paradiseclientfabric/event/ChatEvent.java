package tk.milkthedev.paradiseclientfabric.event;

import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;
import tk.milkthedev.paradiseclientfabric.command.CommandManager;

public class ChatEvent {
    private static final CommandManager commandManager = ParadiseClient_Fabric.getCommandManager();
//    public static boolean outgoingChatMessage(String message)
//    {
//        if (message.startsWith(commandManager.getPrefix()))
//        {
//            commandManager.handleCommand(message);
//            return false; // A command, cancel the event
//        }
//        return true; // Not a command, don't cancel the event
//    }
}
