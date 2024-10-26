package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.WallPaper;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

public class BackgroundCommand extends Command {
    public BackgroundCommand(MinecraftClient minecraftClient) {
        super("background", "", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
        return literal(getName())
                .then(argument("index", IntegerArgumentType.integer(1))
                       .executes(context -> {
                           WallPaper.backgroundImage = Constants.backgroundImages.get(context.getArgument("index", Integer.class));
                           return SINGLE_SUCCESS;
                       }));
    }
}
