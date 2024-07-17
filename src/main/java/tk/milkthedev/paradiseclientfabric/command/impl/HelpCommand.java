package tk.milkthedev.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;
import tk.milkthedev.paradiseclientfabric.command.Command;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("paradisehelp", "Shows help page");
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        LiteralArgumentBuilder<FabricClientCommandSource> node =  literal(getName());

        for (Command command : ParadiseClient_Fabric.getCommandManager().getCommands())
            node.then(literal(command.getName()).executes((context) -> {
                Command c = ParadiseClient_Fabric.getCommandManager().getCommand(context.getInput().split(" ")[1]);
                Helper.printChatMessage("§4§l" + c.getName() + "§r §6" + c.getDescription());
                return SINGLE_SUCCESS;
            }));

        node.executes((context -> {
            for (Command command : ParadiseClient_Fabric.getCommandManager().getCommands())
                Helper.printChatMessage("§4§l" + command.getName() + "§r §6" + command.getDescription());
            return SINGLE_SUCCESS;
        }));

        return node;
    }
}
