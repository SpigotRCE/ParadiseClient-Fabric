package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

public class MotionBlurCommand extends Command {
    public MotionBlurCommand(MinecraftClient minecraftClient) {
        super("motionblur", "The motion blur related commands", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
        return literal(getName())
                .executes(context -> {
                    ParadiseClient_Fabric.getMotionBlurMod().toggle();
                    Helper.printChatMessage(ParadiseClient_Fabric.getMotionBlurMod().disabled ? "Motion blur disabled" : "Motion blur enabled");
                    return SINGLE_SUCCESS;
                })
                .then(argument("value", IntegerArgumentType.integer(1, 100))
                        .executes(context -> {
                            if (ParadiseClient_Fabric.getMotionBlurMod().disabled) {
                                Helper.printChatMessage("Motion blur is disabled");
                                return SINGLE_SUCCESS;
                            }
                            int value = context.getArgument("value", Integer.class);
                            ParadiseClient_Fabric.getMotionBlurMod().blurAmount = value;
                            Helper.printChatMessage("Motion blur set to: " + value);
                            return SINGLE_SUCCESS;
                        })
                );
    }
}
