package io.github.spigotrce.paradiseclientfabric.mixin.inject.chat;

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

/**
 * Mixin for the SuggestionWindow class within ChatInputSuggestor to inject custom behavior.
 * This mixin is used to modify the rendering and scrolling behavior of the suggestion window in Minecraft's chat input.
 * @author SpigotRCE
 * @since 2.15
 */
@Mixin(ChatInputSuggestor.SuggestionWindow.class)
public class SuggestionWindowMixin {

    /** Stores the DrawContext for later use in rendering operations. */
    @Unique
    DrawContext savedContext;

    /** Stores the index before scrolling to keep track of the previous position. */
    @Unique
    int indexBefore;

    /** Offset for pixel scrolling, allowing for smooth scrolling of suggestions. */
    @Unique
    float scrollPixelOffset;

    /** Target index for scrolling, determining where the window should scroll to. */
    @Unique
    int targetIndex;

    /** Shadow field representing the index within the window. */
    @Shadow
    private int inWindowIndex;

    /** Shadow field representing the list of suggestions displayed in the window. */
    @Final
    @Shadow
    private List<Suggestion> suggestions;

    /** Shadow field representing the rectangular area of the suggestion window. */
    @Final
    @Shadow
    private Rect2i area;

    /**
     * Injects custom behavior at the start of the render method to handle scrolling and saving context.
     *
     * @param context The DrawContext used for rendering.
     * @param mouseX  The x-coordinate of the mouse position.
     * @param mouseY  The y-coordinate of the mouse position.
     * @param ci      Callback information for the method.
     */
    @Inject(method = "render", at = @At("HEAD"))
    private void renderHead(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        savedContext = context;
        scrollPixelOffset = (float) (scrollPixelOffset * Math.pow(0.3f, getLastFrameDuration()));
        inWindowIndex = clamp(targetIndex - getScrollOffset() / 12, 0, suggestions.size() - 10);
    }

    /**
     * Injects custom behavior into the render method to apply a scissor mask for rendering.
     *
     * @param context The DrawContext used for rendering.
     * @param mouseX  The x-coordinate of the mouse position.
     * @param mouseY  The y-coordinate of the mouse position.
     * @param ci      Callback information for the method.
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 4))
    private void mask(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        savedContext.enableScissor(0, area.getY(), context.getScaledWindowWidth(), area.getY() + area.getHeight());
    }

    /**
     * Injects custom behavior to remove the scissor mask after rendering text with shadow.
     *
     * @param context The DrawContext used for rendering.
     * @param mouseX  The x-coordinate of the mouse position.
     * @param mouseY  The y-coordinate of the mouse position.
     * @param ci      Callback information for the method.
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I", shift = At.Shift.AFTER))
    private void demask(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        context.disableScissor();
    }

    /**
     * Injects custom behavior at the end of the render method to reset the in-window index.
     *
     * @param context The DrawContext used for rendering.
     * @param mouseX  The x-coordinate of the mouse position.
     * @param mouseY  The y-coordinate of the mouse position.
     * @param ci      Callback information for the method.
     */
    @Inject(method = "render", at = @At("TAIL"))
    private void renderTail(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        inWindowIndex = targetIndex;
    }

    /**
     * Modifies the argument for the Y position when drawing text with shadow to adjust for scrolling offset.
     *
     * @param s The original Y position.
     * @return The modified Y position adjusted by the drawing offset.
     */
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I"), index = 3)
    private int textPosY(int s) {
        return (s + getDrawOffset());
    }

    /**
     * Injects behavior at the start of the mouseScrolled method to save the current index before scrolling.
     *
     * @param am Amount scrolled.
     * @param ci Callback information for the returnable Boolean type.
     */
    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    private void mScrollHead(double am, CallbackInfoReturnable<Boolean> ci) {
        commonSH();
    }

    /**
     * Injects behavior at the end of the mouseScrolled method to update scrolling state after scrolling.
     *
     * @param am Amount scrolled.
     * @param ci Callback information for the returnable Boolean type.
     */
    @Inject(method = "mouseScrolled", at = @At("RETURN"))
    private void mScrollTail(double am, CallbackInfoReturnable<Boolean> ci) {
        commonST();
    }

    /**
     * Injects behavior at the start of the scroll method to save the current index before scrolling.
     *
     * @param off The scroll offset.
     * @param ci  Callback information for the method.
     */
    @Inject(method = "scroll", at = @At("HEAD"))
    private void scrollHead(int off, CallbackInfo ci) {
        commonSH();
    }

    /**
     * Injects behavior at the end of the scroll method to update scrolling state after scrolling.
     *
     * @param off The scroll offset.
     * @param ci  Callback information for the method.
     */
    @Inject(method = "scroll", at = @At("TAIL"))
    private void scrollTail(int off, CallbackInfo ci) {
        commonST();
    }

    /** Saves the current index before scrolling. */
    @Unique
    private void commonSH() {
        indexBefore = inWindowIndex;
    }

    /** Updates the scrolling state after scrolling. */
    @Unique
    private void commonST() {
        scrollPixelOffset += (inWindowIndex - indexBefore) * 12;
        targetIndex = inWindowIndex;
        inWindowIndex = indexBefore;
    }

    /**
     * Modifies a variable to add an extra line above in the suggestion window during rendering.
     *
     * @param r The original line index.
     * @return The modified line index.
     */
    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 4)
    private int addLineAbove(int r) {
        if (getScrollOffset() <= 0 || inWindowIndex <= 0) return (r);
        return (r - 1);
    }

    /**
     * Modifies a variable to add an extra line below in the suggestion window during rendering.
     *
     * @param i The original line index.
     * @return The modified line index.
     */
    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 2)
    private int addLineUnder(int i) {
        if (getScrollOffset() >= 0 || inWindowIndex >= suggestions.size() - 10) return (i);
        return (i + 1);
    }

    /**
     * Calculates the draw offset for rendering text based on the scroll offset.
     *
     * @return The calculated draw offset.
     */
    @Unique
    int getDrawOffset() {
        return Math.round(scrollPixelOffset) - (Math.round(scrollPixelOffset) / 12 * 12);
    }

    /**
     * Gets the total scroll offset in pixels.
     *
     * @return The total scroll offset.
     */
    @Unique
    int getScrollOffset() {
        return Math.round(scrollPixelOffset);
    }

    /**
     * Gets the duration of the last frame for smooth scrolling calculations.
     *
     * @return The duration of the last frame.
     */
    @Unique
    public float getLastFrameDuration() {
        return MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration();
    }

    /**
     * Clamps a value between a minimum and maximum.
     *
     * @param val The value to clamp.
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     * @return The clamped value.
     */
    @Unique
    public int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}
