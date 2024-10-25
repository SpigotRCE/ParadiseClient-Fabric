package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

/**
 * This class represents a command for spamming a specified command in Minecraft.
 * It extends the {@link Command} class and overrides the {@link #build()} method to define the command structure.
 *
 * @author SpigotRCE
 * @since 1.5
 */
public class SpamCommand extends Command {
    /**
     * A static boolean flag indicating whether the spamming is currently running.
     */
    public static boolean isRunning = false;

    /**
     * A thread for executing the spamming process.
     */
    private Thread thread;

    /**
     * Constructs a new SpamCommand instance.
     *
     * @param minecraftClient The Minecraft client instance.
     */
    public SpamCommand(MinecraftClient minecraftClient) {
        super("spam", "Spams the specified command", minecraftClient);
    }

    /**
     * Builds the command structure using Brigadier's LiteralArgumentBuilder.
     *
     * @return The built command structure.
     */
    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
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
                .then(argument("repeation", IntegerArgumentType.integer())
                        .executes((context) -> {
                            Helper.printChatMessage("§4§l" + context.getInput() + "<repeation> <delay> <command>");
                            return SINGLE_SUCCESS;
                        })
                        .then(argument("delay", IntegerArgumentType.integer())
                                .executes((context) -> {
                                    Helper.printChatMessage("§4§l" + context.getInput() + " <command>");
                                    return SINGLE_SUCCESS;
                                })
                                .then(argument("command", StringArgumentType.greedyString())
                                        .executes((context) -> {
                                            int repetition = context.getArgument("repeation", Integer.class);
                                            int delay = context.getArgument("delay", Integer.class);
                                            String command = context.getArgument("command", String.class);
                                            SpamCommand.isRunning = true;
                                            thread = new Thread(() -> {
                                                for (int i = 0; i < repetition; i++) {
                                                    if (!SpamCommand.isRunning) {
                                                        this.thread = null;
                                                        return;
                                                    }
                                                    try {
                                                        Thread.sleep(delay);
                                                    } catch (InterruptedException e) {
                                                        Constants.LOGGER.error("Unable to sleep for 1000ms", e);
                                                    }
                                                    assert getMinecraftClient().player != null;
                                                    getMinecraftClient().player.networkHandler.sendChatCommand(command);
                                                }
                                            });
                                            thread.start();
                                            return SINGLE_SUCCESS;
                                        }))
                                .executes((context) -> {
                                    Helper.printChatMessage("§4§l" + context.getInput() + "<repeation> <delay> <command>");
                                    return SINGLE_SUCCESS;
                                })
                        )
                );
    }
}
