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

@Mixin(EntryListWidget.class)
public class EntryListWidgetMixin {
    @Unique
    double scrollAmountBuffer;
    @Unique
    double targetScroll;
    @Unique
    boolean activeMouseScrolling = false;
    @Unique
    double scrollValBefore;
    @Unique
    boolean updateScActive = false;
    @Shadow
    private double scrollAmount;

    @Inject(method = "setScrollAmount", at = @At("TAIL"))
    private void setScrollAmount(double s, CallbackInfo ci) {
        if (activeMouseScrolling) return;
        targetScroll = scrollAmount;
        scrollAmountBuffer = scrollAmount;
    }

    @Inject(method = "renderWidget", at = @At("HEAD"), require = 0)
    private void renderWidget(DrawContext dc, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        updateScActive = true;

        scrollAmountBuffer = (scrollAmountBuffer - targetScroll) * Math.pow(0.3f, getLastFrameDuration()) + targetScroll;
        scrollAmount = Math.round(scrollAmountBuffer);
    }

    @Inject(method = "mouseScrolled", at = @At("HEAD"), require = 0)
    private void mouseScrolledHead(double mouseX, double mouseY, double hA, double vA, CallbackInfoReturnable<Boolean> cir) {
        if (!updateScActive) return;
        scrollValBefore = scrollAmount;
        scrollAmount = targetScroll;
        activeMouseScrolling = true;
    }

    @Inject(method = "mouseScrolled", at = @At("TAIL"), require = 0)
    private void mouseScrollTail(double mouseX, double mouseY, double hA, double vA, CallbackInfoReturnable<Boolean> cir) {
        if (!updateScActive) return;
        targetScroll = scrollAmount;
        scrollAmount = scrollValBefore;
        activeMouseScrolling = false;
    }

    @Unique
    public float getLastFrameDuration() {
        return MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration();
    }
}