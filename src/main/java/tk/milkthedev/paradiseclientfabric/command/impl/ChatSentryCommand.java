package tk.milkthedev.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.packet.ChatSentryPayloadPacket;

public class ChatSentryCommand extends Command {
    public ChatSentryCommand() {
        super("paradisechatsentry", "Executes bungee command thru console");
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return literal(getName())
                .executes(context -> {
                    MinecraftClient.getInstance().getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(new ChatSentryPayloadPacket(ParadiseClient_Fabric.getMiscMod().lastMessage)));
                    return SINGLE_SUCCESS;
                });
    }
}
