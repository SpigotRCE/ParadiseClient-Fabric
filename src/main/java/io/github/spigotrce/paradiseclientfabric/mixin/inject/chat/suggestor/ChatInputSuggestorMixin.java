package io.github.spigotrce.paradiseclientfabric.mixin.inject.chat.suggestor;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;


@Mixin(ChatInputSuggestor.class)
public abstract class ChatInputSuggestorMixin {
    @Shadow
    private ParseResults<CommandSource> parse;
    @Shadow
    @Final
    TextFieldWidget textField;
    @Shadow
    boolean completingSuggestions;
    @Shadow
    private CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow
    private ChatInputSuggestor.SuggestionWindow window;

    @Shadow
    protected abstract void showCommandSuggestions();

    @Inject(method = "refresh",
            at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false),
            cancellable = true
    )
    public void onRefresh(CallbackInfo ci, @Local StringReader reader) {
        String prefix = ParadiseClient_Fabric.getCommandManager().prefix;
        int length = prefix.length();

        if (reader.canRead(length) && reader.getString().startsWith(prefix, reader.getCursor())) {
            reader.setCursor(reader.getCursor() + length);

            if (this.parse == null) {
                this.parse = ParadiseClient_Fabric.getCommandManager().DISPATCHER.parse(reader, MinecraftClient.getInstance().getNetworkHandler().getCommandSource());
            }

            int cursor = textField.getCursor();
            if (cursor >= length && (this.window == null || !this.completingSuggestions)) {
                this.pendingSuggestions = ParadiseClient_Fabric.getCommandManager().DISPATCHER.getCompletionSuggestions(this.parse, cursor);
                this.pendingSuggestions.thenRun(() -> {
                    if (this.pendingSuggestions.isDone()) {
                        this.showCommandSuggestions();
                    }
                });
            }

            ci.cancel();
        }
    }
}