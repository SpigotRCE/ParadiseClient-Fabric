package tk.milkthedev.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.PlayerListEntry;
import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.command.Command;

import java.util.Objects;

public class PlayersCommand extends Command {
    public PlayersCommand() {
        super("paradiseplayers", "Gets info about players online on the server");
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return literal(getName())
                .executes((context) -> {
                    for (PlayerListEntry player : context.getSource().getPlayer().networkHandler.getPlayerList()) {
                        Helper.printChatMessage(Objects.isNull(player.getDisplayName().getLiteralString()) ? "NULL" : player.getDisplayName().getLiteralString() + " " + player.getProfile().getName() + " " + player.getProfile().getId());
                    }
                    return SINGLE_SUCCESS;
                });
    }
}
