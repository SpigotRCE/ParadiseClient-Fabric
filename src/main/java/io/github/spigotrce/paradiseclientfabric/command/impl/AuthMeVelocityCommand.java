package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import io.github.spigotrce.paradiseclientfabric.packet.AuthMeVelocityPayloadPacket;

public class AuthMeVelocityCommand extends Command {
    public AuthMeVelocityCommand(MinecraftClient minecraftClient) {
        super("paradiseauthmebypass", "Bypasses AuthMe if the server is using AuthMeVelocity", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return literal(getName())
                .executes(context -> {
                    MinecraftClient.getInstance().getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(new AuthMeVelocityPayloadPacket()));
                    return SINGLE_SUCCESS;
                });
    }
}
