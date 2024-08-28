package io.github.spigotrce.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.command.Command;
import net.minecraft.client.MinecraftClient;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CopyCommand extends Command {
    public CopyCommand(MinecraftClient minecraftClient) {
        super("paradisecopy", "Copies the broadcast of SpigotRCE", minecraftClient);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> build() {
        return
                literal(getName())
                        .then(literal("tellraw")
                                .executes((context) -> {
                                    StringSelection stringSelection = new StringSelection("tellraw @a [{\"text\":\"Server hacked by\\n\", \"color\":\"green\"},{\"text\":\"https://youtube.com/@SpigotRCE\", \"color\":\"aqua\", \"bold\":true, \"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://youtube.com/@SpigotRCE\"}}]");
                                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                                    clipboard.setContents(stringSelection, null);
                                    Helper.printChatMessage("SpigotRCE's tellraw has been copied to your clipboard.");
                                    return SINGLE_SUCCESS;
                                }))
                        .executes((context) -> {
                            StringSelection stringSelection = new StringSelection("&aServer hacked by&b&l https://youtube.com/@SpigotRCE");
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            clipboard.setContents(stringSelection, null);
                            Helper.printChatMessage("SpigotRCE's broadcast has been copied to your clipboard.");
                            return SINGLE_SUCCESS;
                        });
    }
}
