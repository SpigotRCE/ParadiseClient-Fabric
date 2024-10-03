package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.sound.SoundCategory;

public class SoundCommand extends Command {
    /**
     * Constructor for the Command class.
     *
     * @param minecraftClient The Minecraft client instance.
     */
    public SoundCommand(MinecraftClient minecraftClient) {
        super("sound", "Plays the sound", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
        LiteralArgumentBuilder<CommandSource> literal = literal(getName());

        ParadiseClient_Fabric.getSoundMod().soundEvents.forEach(s ->
                literal.then(literal(s.getId().getPath())
                        .executes(context -> {
                            getMinecraftClient().player.getWorld().playSound(
                                    getMinecraftClient().player,
                                    getMinecraftClient().player.getBlockPos(),
                                    s, SoundCategory.MUSIC, 1f, 1f
                            );
                            return SINGLE_SUCCESS;
                        })));
        return literal;
    }
}
