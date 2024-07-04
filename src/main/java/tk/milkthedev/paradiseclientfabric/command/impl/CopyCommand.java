package tk.milkthedev.paradiseclientfabric.command.impl;

import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.command.Command;
import tk.milkthedev.paradiseclientfabric.command.CommandInfo;
import tk.milkthedev.paradiseclientfabric.command.exception.CommandException;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
@CommandInfo(
        alias = "copy",
        description = "Copies the broadcast of SpigotRCE to the clipboard.",
        usage = "copy"
)
public class CopyCommand extends Command
{
    @Override
    public boolean execute(String commandAlias, String... args) throws CommandException
    {
        StringSelection stringSelection = new StringSelection("&aServer hacked by&b&l https://youtube.com/@SpigotRCE");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        Helper.printChatMessage("SpigotRCE's broadcast has been copied to your clipboard.");
        return true;
    }

    @Override
    public String[] onTabComplete(String commandAlias, String... args)
    {
        return new String[0];
    }
}
