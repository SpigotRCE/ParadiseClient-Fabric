package tk.milkthedev.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.exploit.Exploit;

public class CrashCommand extends Command {

    public CrashCommand() {
        super("paradisecrash", "Crashes the server");
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        LiteralArgumentBuilder<FabricClientCommandSource> node = literal(getName());

        for (Exploit exploit : ParadiseClient_Fabric.getExploitManager().getExploits())
            node.then(literal(exploit.getAlias())
                    .executes((context) -> {
                        Helper.printChatMessage(context.getInput());
                        ParadiseClient_Fabric.getExploitManager().handleExploit(context.getInput().split(" ")[1]);
                        return SINGLE_SUCCESS;
                    }));

        node.then(literal("off")
                .executes((context) -> {
                    ParadiseClient_Fabric.getExploitMod().isRunning = false;
                    Helper.printChatMessage("[CrashExploit] Stopping all exploits");
                    return SINGLE_SUCCESS;
                }));

        return node;
    }
}
