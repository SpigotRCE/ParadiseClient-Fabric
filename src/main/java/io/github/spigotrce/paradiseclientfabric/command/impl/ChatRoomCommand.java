package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.chatroom.client.Client;
import io.github.spigotrce.paradiseclientfabric.chatroom.client.TokenStore;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.PacketRegistry;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.MessagePacket;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

import java.io.IOException;

public class ChatRoomCommand extends Command {
    public ChatRoomCommand(MinecraftClient minecraftClient) {
        super("chatroom", "Connects you to a chatroom", minecraftClient, true);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
        return literal(getName())
                .executes(context -> {
                    Helper.printChatMessage("Incomplete command!");
                    return SINGLE_SUCCESS;
                })
                .then(literal("say")
                        .executes(context -> {
                            Helper.printChatMessage("Incomplete command!");
                            return SINGLE_SUCCESS;
                        })
                        .then(argument("message", StringArgumentType.greedyString())
                                .executes(context -> {
                                    if (!ParadiseClient_Fabric.CHAT_ROOM_MOD.isConnected) {
                                        Helper.printChatMessage("§4§lError: Not connected to chatroom");
                                        return SINGLE_SUCCESS;
                                    }

                                    String message = context.getArgument("message", String.class);
                                    PacketRegistry.sendPacket(
                                            new MessagePacket(message),
                                            ParadiseClient_Fabric.CHAT_ROOM_MOD.channel
                                    );
                                    return SINGLE_SUCCESS;
                                })
                        )
                )
                .then(literal("token")
                        .executes(context -> {
                            Helper.printChatMessage("Incomplete command!");
                            return SINGLE_SUCCESS;
                        })
                        .then(argument("token", StringArgumentType.greedyString())
                                .executes(context -> {
                                    try {
                                        TokenStore.writeToken(context.getArgument("token", String.class));
                                    } catch (IOException e) {
                                        Helper.printChatMessage("§4§lError: Failed to write token");
                                        Constants.LOGGER.error("Failed to write token", e);
                                        return SINGLE_SUCCESS;
                                    }
                                    return SINGLE_SUCCESS;
                                })
                        )
                )
                .then(literal("connect")
                        .executes(context -> {
                            if (ParadiseClient_Fabric.CHAT_ROOM_MOD.isConnected) {
                                Helper.printChatMessage("You are already connected to chatroom");
                                return SINGLE_SUCCESS;
                            }
                            try {
                                TokenStore.readToken();
                            } catch (IOException e) {
                                Helper.printChatMessage("§4§lError: Failed to read token");
                                Constants.LOGGER.error("Failed to read token", e);
                                return SINGLE_SUCCESS;
                            }
                            try {
                                Client.connected();
                            } catch (Exception e) {
                                Helper.printChatMessage("§4§lError: Failed to connect to chatroom");
                                Constants.LOGGER.error("Failed to connect to chatroom", e);
                            }
                            return SINGLE_SUCCESS;
                        })
                )
                .then(literal("disconnect")
                        .executes(context -> {
                            if (!ParadiseClient_Fabric.CHAT_ROOM_MOD.isConnected) {
                                Helper.printChatMessage("§4§lError: Not connected to chatroom");
                                return SINGLE_SUCCESS;
                            }
                            Client.stop();
                            return SINGLE_SUCCESS;
                        })
                );
    }
}
