package tk.milkthedev.paradiseclientfabric.command.impl;

import net.minecraft.client.MinecraftClient;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.command.CommandException;
import tk.milkthedev.paradiseclientfabric.command.CommandInfo;

import java.util.ArrayList;

@CommandInfo(
        alias = "say",
        description = "Sends a message to the chat",
        usage = ".say <message>"
)
public class SayCommand extends Command
{
    @Override
    public void execute(String commandAlias, String... args) throws CommandException
    {
        StringBuilder s = new StringBuilder();
        for (String arg : args)
        {
            s.append(arg).append(" ");
        }
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.networkHandler.sendChatMessage(s.toString());
    }

    @Override
    public String[] onTabComplete(String commandAlias, String... args) {return new ArrayList<String>().toArray(new String[0]);}
}
