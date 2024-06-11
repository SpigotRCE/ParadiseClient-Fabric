package tk.milkthedev.paradiseclientfabric.command.impl;

import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;
import tk.milkthedev.paradiseclientfabric.chatroom.ClientImpl;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.command.CommandInfo;
import tk.milkthedev.paradiseclientfabric.command.exception.CommandException;

import java.util.ArrayList;
import java.util.Objects;


@CommandInfo(
        alias = "chatroom",
        description = "Connects to the chat room",
        usage = "chatroom <connect|say|quit> [host]"
)
public class ChatRoomCommand extends Command
{
    @Override
    public boolean execute(String commandAlias, String... args) throws CommandException
    {
        String arg0;
        try {arg0 = args[0];} catch (Exception e) {return false;}
        if (arg0.equals("quit"))
        {
            if (!ParadiseClient_Fabric.getChatRoomMod().isConnected) {Helper.printChatMessage("Not connected to any chatroom"); return true;}
            ParadiseClient_Fabric.getChatRoomMod().client.shutdown();
            return true;
        }
        String arg1;
        try {arg1 = args[1];} catch (Exception e) {return false;}
        if (arg0.equals("connect"))
        {
            if (ParadiseClient_Fabric.getChatRoomMod().isConnected) {Helper.printChatMessage("Already connected to a chatroom"); return true;}

            ParadiseClient_Fabric.getChatRoomMod().client = new ClientImpl(arg1, 45096);
            Thread thread = new Thread(ParadiseClient_Fabric.getChatRoomMod().client);
            thread.start();
            return true;
        }
        if (arg0.equals("say"))
        {
            if (!ParadiseClient_Fabric.getChatRoomMod().isConnected) {Helper.printChatMessage("Not connected to any chatroom"); return true;}
            StringBuilder s = new StringBuilder();
            for (int i = 1; i < args.length; i++) {s.append(args[i]).append(" ");}
            ParadiseClient_Fabric.getChatRoomMod().client.sendMessage(s.toString());
            return true;
        }

        return false;
    }

    @Override
    public String[] onTabComplete(String commandAlias, String... args)
    {
        ArrayList<String> suggestions = new ArrayList<>();
        if (args.length == 0) {suggestions.add("connect"); suggestions.add("say"); suggestions.add("quit");}
        if (args.length > 0)
        {
            if (args[0].equals("connect")) {suggestions.add("127.0.0.1");}
            if (args[0].equals("say") || (args[0].equals("quit"))) {return suggestions.toArray(new String[0]);}
        }
        return suggestions.toArray(new String[0]);
    }
}
