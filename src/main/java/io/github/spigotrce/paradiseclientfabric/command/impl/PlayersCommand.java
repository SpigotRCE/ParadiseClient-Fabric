package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a command to get information about players online on the server.
 *
 * @author Spigotrce
 * @since 2.12
 */
public class PlayersCommand extends Command {

    /**
     * Constructs a new instance of PlayersCommand.
     *
     * @param minecraftClient The Minecraft client instance.
     */
    public PlayersCommand(MinecraftClient minecraftClient) {
        super("players", "Gets info about players online on the server", minecraftClient);
    }

    /**
     * Builds the command using Brigadier library.
     *
     * @return The built command.
     */
    @Override
    public LiteralArgumentBuilder<CommandSource> build() {
        return literal(getName())
                .executes((context) -> {
                    Map<String, PlayerData> playerDataMap = new HashMap<>();

                    getMinecraftClient().player.networkHandler.getPlayerList().forEach(playerInfo -> {
                        String playerName = playerInfo.getProfile().getName();
                        String playerUUID = playerInfo.getProfile().getId().toString();
                        String playerGamemode = playerInfo.getGameMode().getName();
                        int playerPing = playerInfo.getLatency();
                        PlayerData playerData = new PlayerData(playerName, playerUUID, playerGamemode, playerPing);
                        playerDataMap.put(playerName, playerData);
                    });

                    if (playerDataMap.isEmpty())
                        getMinecraftClient().player.sendMessage(Helper.parseColoredText("No players"), true);

                    playerDataMap.forEach((name, playerData) -> getMinecraftClient().player.sendMessage(playerData.getMessage(), false));
                    return SINGLE_SUCCESS;
                });
    }

    /**
     * Represents player data.
     */
    public static class PlayerData {
        final String name;
        final String uuid;
        final String gameMode;
        final int ping;

        /**
         * Constructs a new instance of PlayerData.
         *
         * @param name     The player's name.
         * @param uuid     The player's UUID.
         * @param gameMode The player's game mode.
         * @param ping     The player's ping.
         */
        public PlayerData(String name, String uuid, String gameMode, int ping) {
            this.name = name;
            this.uuid = uuid;
            this.gameMode = gameMode;
            this.ping = ping;
        }

        /**
         * Gets the player's information as a formatted message.
         *
         * @return The formatted message.
         */
        public Text getMessage() {
            Text nameText = Helper.parseColoredText("&7" + name);
            Text uuidText = Helper.parseColoredText(" &8[&bCopy UUID&8]", uuid);
            Text gamemodeText = Helper.parseColoredText(" &8(" + getGameModeColor() + Helper.capitalizeFirstLetter(gameMode) + "&8)");
            Text pingText = Helper.parseColoredText(" &8(&a" + ping + "ms&8)");
            return Text.empty()
                    .append(nameText)
                    .append(uuidText)
                    .append(gamemodeText)
                    .append(pingText);
        }

        /**
         * Gets the color code for the player's game mode.
         *
         * @return The color code.
         */
        public String getGameModeColor() {
            return switch (gameMode.toLowerCase()) {
                case "survival" -> "&c";
                case "creative" -> "&a";
                case "adventure" -> "&b";
                default -> "&7";
            };
        }
    }
}
