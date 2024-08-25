package tk.milkthedev.paradiseclientfabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public abstract class Command {
    protected static final int SINGLE_SUCCESS = com.mojang.brigadier.Command.SINGLE_SUCCESS;
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    abstract public LiteralArgumentBuilder<FabricClientCommandSource> build();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    protected LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }
}
