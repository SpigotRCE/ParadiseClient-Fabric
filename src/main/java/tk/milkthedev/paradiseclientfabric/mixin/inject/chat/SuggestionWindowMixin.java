package tk.milkthedev.paradiseclientfabric.mixin.inject.chat;

import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.util.math.Rect2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ChatInputSuggestor.SuggestionWindow.class)
public class SuggestionWindowMixin {
    @Unique
    DrawContext savedContext;
    @Unique
    int indexBefore;
    @Unique
    float scrollPixelOffset;
    @Unique
    int targetIndex;
    @Shadow
    private int inWindowIndex;
    @Final
    @Shadow
    private List<Suggestion> suggestions;
    @Final
    @Shadow
    private Rect2i area;

    @Inject(method = "render", at = @At("HEAD"))
    private void renderHead(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        savedContext = context;
        scrollPixelOffset = (float) (scrollPixelOffset * Math.pow(0.3f, getLastFrameDuration()));
        inWindowIndex = clamp(targetIndex - getScrollOffset() / 12, 0, suggestions.size() - 10);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 4))
    private void mask(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        savedContext.enableScissor(0, area.getY(), context.getScaledWindowWidth(), area.getY() + area.getHeight());
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I", shift = At.Shift.AFTER))
    private void demask(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        context.disableScissor();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderTail(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        inWindowIndex = targetIndex;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I"), index = 3)
    private int textPosY(int s) {
        return (s + getDrawOffset());
    }

    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    private void mScrollHead(double am, CallbackInfoReturnable<Boolean> ci) {
        commonSH();
    }

    @Inject(method = "mouseScrolled", at = @At("RETURN"))
    private void mScrollTail(double am, CallbackInfoReturnable<Boolean> ci) {
        commonST();
    }

    @Inject(method = "scroll", at = @At("HEAD"))
    private void scrollHead(int off, CallbackInfo ci) {
        commonSH();
    }

    @Inject(method = "scroll", at = @At("TAIL"))
    private void scrollTail(int off, CallbackInfo ci) {
        commonST();
    }

    @Unique
    private void commonSH() {
        indexBefore = inWindowIndex;
    }

    @Unique
    private void commonST() {
        scrollPixelOffset += (inWindowIndex - indexBefore) * 12;
        targetIndex = inWindowIndex;
        inWindowIndex = indexBefore;
    }

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 4)
    private int addLineAbove(int r) {
        if (getScrollOffset() <= 0 || inWindowIndex <= 0) return (r);
        return (r - 1);
    }

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 2)
    private int addLineUnder(int i) {
        if (getScrollOffset() >= 0 || inWindowIndex >= suggestions.size() - 10) return (i);
        return (i + 1);
    }

    @Unique
    int getDrawOffset() {
        return Math.round(scrollPixelOffset) - (Math.round(scrollPixelOffset) / 12 * 12);
    }

    @Unique
    int getScrollOffset() {
        return Math.round(scrollPixelOffset);
    }

    @Unique
    public float getLastFrameDuration() {
        return MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration();
    }

    @Unique
    public int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}
