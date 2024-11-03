package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import io.github.spigotrce.paradiseclientfabric.mixin.accessor.ClientPlayNetworkHandlerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

public class SayCommand extends Command {
    public SayCommand(MinecraftClient minecraftClient) {
        super("say", "Sends a chat message to the server", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
        return literal(getName())
                .then(argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                                    String message = context.getArgument("message", String.class);
                                    ((ClientPlayNetworkHandlerAccessor) MinecraftClient.getInstance().getNetworkHandler())
                                            .paradiseClient_Fabric$sendChatMessage(message);
                                    return SINGLE_SUCCESS;
                                }
                        )
                );
    }
}
