package tk.milkthedev.paradiseclientfabric.mixin.inject;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Mixin(ChatInputSuggestor.class)
public abstract class ChatInputSuggestorMixin
{
    @Shadow private ParseResults<CommandSource> parse;
    @Shadow @Final TextFieldWidget textField;
    @Shadow boolean completingSuggestions;
    @Shadow private CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow private ChatInputSuggestor.SuggestionWindow window;

    @Unique
    private static CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();

    @Shadow
    protected abstract void showCommandSuggestions();

    // Got brain fucked so used meteor's code don't mind me
    @Inject(method = "refresh",
            at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void onRefresh(CallbackInfo ci, String string, StringReader reader)
    {
        String prefix = "?";
        int length = prefix.length();

        if (reader.canRead(length) && reader.getString().startsWith(prefix, reader.getCursor()))
        {
            reader.setCursor(reader.getCursor() + length);

            if (this.parse == null)
            {
                this.parse = DISPATCHER.parse(reader, Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getCommandSource());
            }

            int cursor = textField.getCursor();
            if ((this.window == null || !this.completingSuggestions))
            {
                DISPATCHER = ParadiseClient_Fabric.getCommandManager().getCommandDispatcher(reader, DISPATCHER);
                this.pendingSuggestions = DISPATCHER.getCompletionSuggestions(this.parse, cursor);
                this.pendingSuggestions.thenRun(() ->
                {
                    if (this.pendingSuggestions.isDone())
                    {
                        this.showCommandSuggestions();
                    }
                });
            }

            ci.cancel();
        }
    }
}
