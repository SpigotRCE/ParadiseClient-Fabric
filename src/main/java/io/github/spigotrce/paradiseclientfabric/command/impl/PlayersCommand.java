package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.command.Command;

import java.util.HashMap;
import java.util.Map;

public class PlayersCommand extends Command {
    public PlayersCommand() {
        super("paradiseplayers", "Gets info about players online on the server");
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return literal(getName())
                .executes((context) -> {
                    Map<String, PlayerData> playerDataMap = new HashMap<>();

                    context.getSource().getPlayer().networkHandler.getPlayerList().forEach(playerInfo -> {
                        String playerName = playerInfo.getProfile().getName();
                        String playerUUID = playerInfo.getProfile().getId().toString();
                        String playerGamemode = playerInfo.getGameMode().getName();
                        int playerPing = playerInfo.getLatency();
                        PlayerData playerData = new PlayerData(playerName, playerUUID, playerGamemode, playerPing);
                        playerDataMap.put(playerName, playerData);
                    });

                    if (playerDataMap.isEmpty())
                        context.getSource().getPlayer().sendMessage(Helper.parseColoredText("No players"), true);

                    playerDataMap.forEach((name, playerData) -> context.getSource().getPlayer().sendMessage(playerData.getMessage(), false));
                    return SINGLE_SUCCESS;
                });
    }

    public static class PlayerData {
        String name;
        String uuid;
        String gameMode;
        int ping;

        public PlayerData(String name, String uuid, String gameMode, int ping) {
            this.name = name;
            this.uuid = uuid;
            this.gameMode = gameMode;
            this.ping = ping;
        }

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
