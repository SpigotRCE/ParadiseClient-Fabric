package tk.milkthedev.paradiseclientfabric.command.impl;

import net.minecraft.client.MinecraftClient;
import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.command.CommandInfo;
import tk.milkthedev.paradiseclientfabric.command.exception.CommandException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static tk.milkthedev.paradiseclientfabric.Helper.isNumber;

@CommandInfo(
        alias = "spam",
        description = "Spams the chat",
        usage = "spam <delay|stop> <repetition> <command>"
)
public class SpamCommand extends Command
{
    public static Thread thread;
    public static boolean isRunning = false;

    @Override
    public boolean execute(String commandAlias, String... args) throws CommandException
    {
        int delay;
        int repetition;
        StringBuilder command;

        try
        {
            if (Objects.equals(args[0], "stop"))
            {
                if (!isRunning)
                {
                    Helper.printChatMessage("Spam is not running");
                    return true;
                }
                isRunning = false;
                return true;
            }
            delay = Integer.parseInt(args[0]);
            repetition = Integer.parseInt(args[1]);
            command = new StringBuilder();
        }
        catch (Exception e)
        {
            return false;
        }
        for (String arg : Arrays.copyOfRange(args, 2, args.length))
        {
            command.append(arg).append(" ");
        }
        isRunning = true;
        thread = new Thread(() ->
        {
            for (int i = 0; i < repetition; i++)
            {
                if (!SpamCommand.isRunning)
                {
                    SpamCommand.thread = null;
                    return;
                }
                try {Thread.sleep(delay);}
                catch (InterruptedException e) {e.printStackTrace();}
                assert MinecraftClient.getInstance().player!= null;
                MinecraftClient.getInstance().player.networkHandler.sendChatCommand(command.toString());
            }
        });
        thread.start();
        return true;
    }

    @Override
    public String[] onTabComplete(String commandAlias, String... args)
    {
        ArrayList<String> suggestions = new ArrayList<>();
        if (args.length == 0)
        {
            suggestions.add("10");
            suggestions.add("stop");
        }
        if (args.length == 1)
        {
            if (!isNumber(args[args.length - 1]))
                suggestions.add("10");
        }
        if (args.length == 2)
        {
            if (!isNumber(args[args.length - 1]))
                suggestions.add("10");
        }
        if (args.length == 3)
        {
            suggestions.add("say Hi");
            suggestions.add("/sphere 0 10");
        }
        return suggestions.toArray(new String[0]);
    }
}
