package tk.milkthedev.paradiseclientfabric.command;

import org.apache.commons.lang3.Validate;
import tk.milkthedev.paradiseclientfabric.command.exception.CommandException;


public abstract class Command
{
    private final String alias;
    private final String description;
    private final String usage;

    public Command()
    {
        CommandInfo commandInfo = this.getClass().getDeclaredAnnotation(CommandInfo.class);
        Validate.notNull(commandInfo, "CONFUSED ANNOTATION EXCEPTION");

        this.alias = commandInfo.alias();
        this.description = commandInfo.description();
        this.usage = commandInfo.usage();
    }

    public abstract boolean execute(String commandAlias, String... args) throws CommandException;
    public abstract String[] onTabComplete(String commandAlias, String... args);

    public String getAlias()
    {
        return alias;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUsage()
    {
        return usage;
    }
}
