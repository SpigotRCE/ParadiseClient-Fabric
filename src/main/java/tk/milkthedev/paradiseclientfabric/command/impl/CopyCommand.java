package tk.milkthedev.paradiseclientfabric.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.command.Command;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CopyCommand extends Command {
    public CopyCommand() {
        super("paradisecopy", "Copies the broadcast of SpigotRCE");
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
