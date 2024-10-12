package io.github.spigotrce.paradiseclientfabric.mixin.inject.chat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

/**
 * A Mixin class that modifies the behavior of the Minecraft ChatHud for custom rendering and scrolling effects.
 * This class is designed to inject custom code into the ChatHud class to implement features such as smooth scrolling,
 * mask height adjustment, and translation corrections during the rendering process.
 * <p>
 * The mixin alters the default behavior of methods related to rendering, scrolling, and message handling to enhance
 * chat functionality and visual effects.
 *
 * @author SpigotRCE
 * @since 2.15
 */
@Mixin(value = ChatHud.class, priority = 1001)
public class ChatHudMixin {

    /**
     * Represents the offset for smooth scrolling in the chat.
     */
    @Unique
    float scrollOffset;

    /**
     * Buffer to adjust the height of the chat mask for rendering purposes.
     */
    @Unique
    float maskHeightBuffer;

    /**
     * Flag indicating whether the chat is currently in a refreshing state.
     */
    @Unique
    boolean refreshing = false;

    /**
     * Stores the scroll value before modification for potential restoration.
     */
    @Unique
    int scrollValBefore;

    /**
     * Holds the current drawing context used in rendering the chat.
     */
    @Unique
    DrawContext savedContext;

    /**
     * Holds the current tick value to synchronize rendering updates.
     */
    @Unique
    int savedCurrentTick;

    /**
     * A vector representing the matrix translation adjustments.
     */
    @Unique
    Vec2f mtc = new Vec2f(0, 0);

    /**
     * The number of lines that have been scrolled in the chat.
     */
    @Shadow
    private int scrolledLines;

    /**
     * A list of currently visible chat messages.
     */
    @Final
    @Shadow
    private List<ChatHudLine.Visible> visibleMessages;

    /**
     * Injects custom logic at the start of the render method to handle context saving and scrolling calculations.
     */
    @Inject(method = "render", at = @At("HEAD"))
    public void renderH(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        savedContext = context;
        savedCurrentTick = currentTick;
        scrollOffset = (float) (scrollOffset * Math.pow(0.3f, getLastFrameDuration()));
        scrollValBefore = scrolledLines;
        scrolledLines -= getChatScrollOffset() / getLineHeight();
        if (scrolledLines < 0) scrolledLines = 0;
    }

    /**
     * Modifies arguments of the matrix translation to correct rendering positions.
     */
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0))
    private void matrixTranslateCorrector(Args args) {
        int x = (int) (float) args.get(0) - 4;
        int y = (int) (float) args.get(1);
        var newY = (float) ((mtc.y - y) * Math.pow(0.3f, getLastFrameDuration()) + y);
        args.set(1, (float) Math.round(newY));
        mtc = new Vec2f(x, newY);
    }

    /**
     * Adjusts the mask height dynamically during rendering based on chat state.
     */
    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 7)
    private int mask(int m) {
        var shownLineCount = 0;
        for (int r = 0; r + scrolledLines < visibleMessages.size() && r < getVisibleLineCount(); r++) {
            if (savedCurrentTick - visibleMessages.get(r).addedTime() < 200 || isChatFocused()) shownLineCount++;
        }
        var targetHeight = shownLineCount * getLineHeight();
        maskHeightBuffer = (float) ((maskHeightBuffer - targetHeight) * Math.pow(0.3f, getLastFrameDuration()) + targetHeight);
        var masktop = m - Math.round(maskHeightBuffer) + (int) mtc.y;
        var maskbottom = m + (int) mtc.y;
        if (getChatScrollOffset() == 0 && Math.round(maskHeightBuffer) != 0) {
            if (Math.round(maskHeightBuffer) == targetHeight) {
                maskbottom += 2;
                masktop -= 2;
            } else {
                maskbottom += 2;
            }
        }
        if (FabricLoader.getInstance().getObjectShare().get("raised:chat") instanceof Integer distance) {
            masktop -= distance;
            maskbottom -= distance;
        }
        savedContext.enableScissor(0, masktop, savedContext.getScaledWindowWidth(), maskbottom);
        return m;
    }

    /**
     * Overrides opacity calculation for rendering transparency effects.
     */
    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 14)
    private int opacity(int t) {
        return 0;
    }

    /**
     * Adjusts the Y position during rendering for proper chat positioning.
     */
    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 18)
    private int changePosY(int y) {
        return y - getChatDrawOffset();
    }

    /**
     * Disables the scissor test after rendering to clean up OpenGL state.
     */
    @ModifyVariable(method = "render", at = @At("STORE"))
    private long demask(long a) {
        savedContext.disableScissor();
        return a;
    }

    /**
     * Restores the scrolled lines state after rendering.
     */
    @Inject(method = "render", at = @At("TAIL"))
    public void renderT(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        scrolledLines = scrollValBefore;
    }

    /**
     * Adjusts scrolling behavior to account for new messages.
     */
    @ModifyVariable(method = "addVisibleMessage", at = @At("STORE"), ordinal = 0)
    List<OrderedText> onNewMessage(List<OrderedText> ot) {
        if (refreshing) return ot;
        scrollOffset -= ot.size() * getLineHeight();
        return ot;
    }

    /**
     * Captures scroll state before modification.
     */
    @Inject(method = "scroll", at = @At("HEAD"))
    public void scrollH(int scroll, CallbackInfo ci) {
        scrollValBefore = scrolledLines;
    }

    /**
     * Updates scroll offset after scrolling action.
     */
    @Inject(method = "scroll", at = @At("TAIL"))
    public void scrollT(int scroll, CallbackInfo ci) {
        scrollOffset += (scrolledLines - scrollValBefore) * getLineHeight();
    }

    /**
     * Captures scroll state before resetting scroll.
     */
    @Inject(method = "resetScroll", at = @At("HEAD"))
    public void scrollResetH(CallbackInfo ci) {
        scrollValBefore = scrolledLines;
    }

    /**
     * Updates scroll offset after resetting scroll.
     */
    @Inject(method = "resetScroll", at = @At("TAIL"))
    public void scrollResetT(CallbackInfo ci) {
        scrollOffset += (scrolledLines - scrollValBefore) * getLineHeight();
    }

    /**
     * Adds additional lines above the current scroll position based on mask height.
     */
    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;getLineHeight()I"), ordinal = 3)
    private int addLinesAbove(int i) {
        return (int) Math.ceil(Math.round(maskHeightBuffer) / (float) getLineHeight()) + (getChatScrollOffset() < 0 ? 1 : 0);
    }

    /**
     * Adjusts the number of lines rendered under the current position based on scroll state.
     */
    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 12)
    private int addLinesUnder(int r) {
        if (scrolledLines == 0 || getChatScrollOffset() <= 0) return r;
        return r - 1;
    }

    /**
     * Marks the chat as refreshing at the start of the refresh process.
     */
    @Inject(method = "refresh", at = @At("HEAD"))
    private void refreshH(CallbackInfo ci) {
        refreshing = true;
    }

    /**
     * Marks the chat as no longer refreshing at the end of the refresh process.
     */
    @Inject(method = "refresh", at = @At("TAIL"))
    private void refreshT(CallbackInfo ci) {
        refreshing = false;
    }

    @Inject(method = "clear", at = @At("HEAD"), cancellable = true)
    public void clear(boolean clearHistory, CallbackInfo ci) {
        ci.cancel();
    }

    /**
     * Shadow method to retrieve the line height of chat messages.
     */
    @Shadow
    private int getLineHeight() {
        return 0;
    }

    /**
     * Shadow method to retrieve the count of visible lines in the chat.
     */
    @Shadow
    public int getVisibleLineCount() {
        return 0;
    }

    /**
     * Shadow method to check if the chat is currently focused.
     */
    @Shadow
    public boolean isChatFocused() {
        return false;
    }

    /**
     * Calculates the offset for drawing chat messages based on current scroll position.
     */
    @Unique
    int getChatDrawOffset() {
        return Math.round(scrollOffset) - (Math.round(scrollOffset) / getLineHeight() * getLineHeight());
    }

    /**
     * Retrieves the current scroll offset for the chat.
     */
    @Unique
    int getChatScrollOffset() {
        return Math.round(scrollOffset);
    }

    /**
     * Retrieves the duration of the last frame for smooth animations.
     */
    @Unique
    public float getLastFrameDuration() {
        return MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration();
    }
}

