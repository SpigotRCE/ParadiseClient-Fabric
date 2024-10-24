package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import io.github.spigotrce.paradiseclientfabric.packet.AuthMeVelocityPayloadPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

public class AuthMeVelocityBypassCommand extends Command {
    public AuthMeVelocityBypassCommand(MinecraftClient minecraftClient) {
        super("authmevelocitybypass", "Bypasses AuthMeVelocity", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
        return literal(getName())
                .executes(context -> {
                    Helper.sendPacket(new CustomPayloadC2SPacket(
                            new AuthMeVelocityPayloadPacket()
                    ));
                    Helper.printChatMessage("Payload packet sent!");
                    return Command.SINGLE_SUCCESS;
                });
    }
}
