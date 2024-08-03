package tk.milkthedev.paradiseclientfabric.mixin.inject.chat;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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

    // Got brain fucked so used meteor's code don't mind me
//    @Inject(method = "refresh",
//            at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false),
//            cancellable = true,
//            locals = LocalCapture.CAPTURE_FAILHARD)
//    public void onRefresh(CallbackInfo ci, String string, StringReader reader)
//    {
//        String prefix = ParadiseClient_Fabric.getCommandManager().getPrefix();
//        int length = prefix.length();
//
//        if (reader.canRead(length) && reader.getString().startsWith(prefix, reader.getCursor()))
//        {
//            reader.setCursor(reader.getCursor() + length);
//
//            if (this.parse == null)
//                this.parse = ParadiseClient_Fabric.getCommandManager().DISPATCHER.parse(reader, Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getCommandSource());
//
//
//            int cursor = textField.getCursor();
//            if (cursor >= length && (this.window == null || !this.completingSuggestions))
//            {
//                this.pendingSuggestions = ParadiseClient_Fabric.getCommandManager().DISPATCHER.getCompletionSuggestions(this.parse, cursor);
//                this.pendingSuggestions.thenRun(() ->
//                {
//                    if (this.pendingSuggestions.isDone())
//                        this.showCommandSuggestions();
//                });
//            }
//
//            ci.cancel();
//        }
//    }
}
