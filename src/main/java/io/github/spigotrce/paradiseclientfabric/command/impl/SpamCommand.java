package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.command.Command;

public class SpamCommand extends Command {
    public static boolean isRunning = false;
    private Thread thread;
    public SpamCommand(MinecraftClient minecraftClient) {
        super("paradisespam", "Spams the specified command", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return literal(getName())
                .then(literal("stop")
                        .executes((context) -> {
                            if (!isRunning) {
                                Helper.printChatMessage("Spam is not running");
                                return SINGLE_SUCCESS;
                            }
                            isRunning = false;
                            return SINGLE_SUCCESS;
                        }))
                .then(literal("10")
                        .executes((context) -> {
                            Helper.printChatMessage("§4§l" + context.getInput() + "<repetion> <command>");
                            return SINGLE_SUCCESS;
                        })
                        .then(literal("100")
                                .executes((context) -> {
                                    Helper.printChatMessage("§4§l" + context.getInput() + "<command>");
                                    return SINGLE_SUCCESS;
                                })
                                .then(literal("command")
                                        .executes((context) -> {
                                            System.out.println("Exec");
                                            SpamCommand.isRunning = true;
                                            thread = new Thread(() -> {
                                                for (int i = 0; i < Integer.parseInt(context.getInput().split(" ")[2]); i++) {
                                                    if (!SpamCommand.isRunning) {
                                                        this.thread = null;
                                                        return;
                                                    }
                                                    try {
                                                        Thread.sleep(Integer.parseInt(context.getInput().split(" ")[1]));
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    assert MinecraftClient.getInstance().player != null;
                                                    MinecraftClient.getInstance().player.networkHandler.sendChatCommand(context.getInput().split(" ")[3]);
                                                }
                                            });
                                            thread.start();
                                            return SINGLE_SUCCESS;
                                        }))
                                .executes((context) -> {
                                    Helper.printChatMessage("§4§l" + context.getInput() + "<repetion> <delay> <command>");
                                    return SINGLE_SUCCESS;
                                })));

    }
}
