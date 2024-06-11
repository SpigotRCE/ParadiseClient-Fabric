package tk.milkthedev.paradiseclientfabric.command.impl;

import net.minecraft.client.MinecraftClient;
import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.command.exception.CommandException;
import tk.milkthedev.paradiseclientfabric.command.CommandInfo;

import java.util.ArrayList;
import java.util.Objects;

@CommandInfo(
        alias = "grief",
        description = "Griefs the world",
        usage = "grief <fill|sphere|tpall> <block>"
)
public class GriefCommand extends Command
{
    @Override
    public boolean execute(String commandAlias, String... args) throws CommandException
    {
        if (args.length > 2 || args.length == 0)
        {
            return false;
        }
        String arg0 = args[0];

        if (arg0.equals("tpall"))
        {
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("tpall");
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("etpall");
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("minecraft:tp @a @p");
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("tp @a @p");
            return true;
        }

        String arg1 = args[1];

        if (arg0.equals("fill"))
        {
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("minecraft:fill ~10 ~10 ~10 ~-10 ~-10 ~-10 " + arg1);
        } else if (arg0.equals("sphere"))
        {
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("/sphere " + arg1 + " 10");
        } else {Helper.printChatMessage("Usage: " + this.getUsage());}
        return true;
    }

    @Override
    public String[] onTabComplete(String commandAlias, String... args)
    {
        ArrayList<String> suggestions = new ArrayList<>();
        if (args.length == 0)
        {
            suggestions.add("fill");
            suggestions.add("sphere");
            suggestions.add("tpall");
            return suggestions.toArray(new String[0]);
        }
        if (args.length == 1 && (Objects.equals(args[0], "fill") || Objects.equals(args[0], "sphere")))
        {
            suggestions.add("air");
            suggestions.add("lava");
            suggestions.add("water");
            suggestions.add("water");
            return suggestions.toArray(new String[0]);
        }
        if (args.length == 1 && Objects.equals(args[0], "tpall"))
        {
            return suggestions.toArray(new String[0]);
        }
        return new String[0];
    }
}
