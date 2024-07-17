package tk.milkthedev.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;
import tk.milkthedev.paradiseclientfabric.command.Command;

public class ScreenShareCommand extends Command {
    public ScreenShareCommand() {
        super("paradisescreenshare", "Toggles IP displayed on HUD");
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return literal(getName()).executes((context -> {
            ParadiseClient_Fabric.getHudMod().showServerIP = !ParadiseClient_Fabric.getHudMod().showServerIP;
            Helper.printChatMessage(ParadiseClient_Fabric.getHudMod().showServerIP ? "Server IP shown" : "Server IP hidden");
            return SINGLE_SUCCESS;
        }));
    }
}
