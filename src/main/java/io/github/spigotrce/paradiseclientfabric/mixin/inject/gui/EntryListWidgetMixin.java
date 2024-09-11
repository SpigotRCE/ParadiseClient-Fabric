package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for the EntryListWidget class to inject custom behavior.
 * This mixin is used to add smooth scrolling to the entry list widget in Minecraft's GUI.
 *
 * @author SpigotRCE
 * @since 2.15
 */
@Mixin(EntryListWidget.class)
public class EntryListWidgetMixin {

    /**
     * Buffer for scroll amount to facilitate smooth scrolling.
     */
    @Unique
    double scrollAmountBuffer;

    /**
     * The target scroll amount the list should move to.
     */
    @Unique
    double targetScroll;

    /**
     * Flag to indicate if mouse scrolling is active.
     */
    @Unique
    boolean activeMouseScrolling = false;

    /**
     * Stores the scroll value before any scrolling operation begins.
     */
    @Unique
    double scrollValBefore;

    /**
     * Flag to indicate if scroll update is active.
     */
    @Unique
    boolean updateScActive = false;

    /**
     * Shadow field representing the current scroll amount of the entry list widget.
     */
    @Shadow
    private double scrollAmount;

    /**
     * Injects behavior at the end of the setScrollAmount method to update target scroll and buffer.
     *
     * @param s  The new scroll amount to set.
     * @param ci Callback information for the method.
     */
    @Inject(method = "setScrollAmount", at = @At("TAIL"))
    private void setScrollAmount(double s, CallbackInfo ci) {
        if (activeMouseScrolling) return;
        targetScroll = scrollAmount;
        scrollAmountBuffer = scrollAmount;
    }

    /**
     * Injects behavior at the start of the renderWidget method to update the scroll animation.
     *
     * @param dc     The DrawContext used for rendering.
     * @param mouseX The x-coordinate of the mouse position.
     * @param mouseY The y-coordinate of the mouse position.
     * @param delta  The frame time delta.
     * @param ci     Callback information for the method.
     */
    @Inject(method = "renderWidget", at = @At("HEAD"), require = 0)
    private void renderWidget(DrawContext dc, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        updateScActive = true;

        scrollAmountBuffer = (scrollAmountBuffer - targetScroll) * Math.pow(0.3f, getLastFrameDuration()) + targetScroll;
        scrollAmount = Math.round(scrollAmountBuffer);
    }

    /**
     * Injects behavior at the start of the mouseScrolled method to handle scrolling start.
     *
     * @param mouseX The x-coordinate of the mouse position.
     * @param mouseY The y-coordinate of the mouse position.
     * @param hA     The horizontal amount scrolled.
     * @param vA     The vertical amount scrolled.
     * @param cir    Callback information for the returnable Boolean type.
     */
    @Inject(method = "mouseScrolled", at = @At("HEAD"), require = 0)
    private void mouseScrolledHead(double mouseX, double mouseY, double hA, double vA, CallbackInfoReturnable<Boolean> cir) {
        if (!updateScActive) return;
        scrollValBefore = scrollAmount;
        scrollAmount = targetScroll;
        activeMouseScrolling = true;
    }

    /**
     * Injects behavior at the end of the mouseScrolled method to handle scrolling end.
     *
     * @param mouseX The x-coordinate of the mouse position.
     * @param mouseY The y-coordinate of the mouse position.
     * @param hA     The horizontal amount scrolled.
     * @param vA     The vertical amount scrolled.
     * @param cir    Callback information for the returnable Boolean type.
     */
    @Inject(method = "mouseScrolled", at = @At("TAIL"), require = 0)
    private void mouseScrollTail(double mouseX, double mouseY, double hA, double vA, CallbackInfoReturnable<Boolean> cir) {
        if (!updateScActive) return;
        targetScroll = scrollAmount;
        scrollAmount = scrollValBefore;
        activeMouseScrolling = false;
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
}
