package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;

public class DumpCommand extends Command {
    public DumpCommand(MinecraftClient minecraftClient) {
        super("dump", "IP dumping methods", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
        return literal(getName())
                .executes(context -> {
                    Helper.sendPacket(new RequestCommandCompletionsC2SPacket(1234689045, "/ip "));
                    Helper.printChatMessage("Attempting to dump IPs via bungee /ip method!");
                    return Command.SINGLE_SUCCESS;
                });
    }
}

