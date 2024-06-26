package tk.milkthedev.paradiseclientfabric.command.impl;

import net.minecraft.client.MinecraftClient;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.command.exception.CommandException;
import tk.milkthedev.paradiseclientfabric.command.CommandInfo;

import java.util.ArrayList;
import java.util.Arrays;

@CommandInfo(
        alias = "say",
        description = "Sends a message to the chat",
        usage = "say <message>"
)
public class SayCommand extends Command
{
    @Override
    public boolean execute(String commandAlias, String... args) throws CommandException
    {
        StringBuilder s = new StringBuilder();
        for (String arg : args) {s.append(arg).append(" ");}
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.networkHandler.sendChatMessage(s.toString());
        return true;
    }

    @Override
    public String[] onTabComplete(String commandAlias, String... args) {return new String[0];}
}
