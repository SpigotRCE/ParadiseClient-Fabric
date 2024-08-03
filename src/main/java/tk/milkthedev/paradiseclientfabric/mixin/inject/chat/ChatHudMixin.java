package tk.milkthedev.paradiseclientfabric.mixin.inject.chat;

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

@Mixin(value = ChatHud.class, priority = 1001) // just above the default priority
public class ChatHudMixin {
    @Shadow
    private int scrolledLines;
    @Final
    @Shadow
    private List<ChatHudLine.Visible> visibleMessages;

    @Unique
    float scrollOffset;
    @Unique
    float maskHeightBuffer;
    @Unique
    boolean refreshing = false;
    @Unique
    int scrollValBefore;
    @Unique
    DrawContext savedContext;
    @Unique
    int savedCurrentTick;
    @Unique
    Vec2f mtc = new Vec2f(0, 0); // matrix translate

    @Inject(method = "render", at = @At("HEAD"))
    public void renderH(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        savedContext = context;
        savedCurrentTick = currentTick;

        scrollOffset = (float) (scrollOffset * Math.pow(0.5f, getLastFrameDuration()));

        scrollValBefore = scrolledLines;
        scrolledLines -= getChatScrollOffset() / getLineHeight();
        if (scrolledLines < 0) scrolledLines = 0;
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0))
    private void matrixTranslateCorrector(Args args) {
        int x = (int) (float) args.get(0) - 4;
        int y = (int) (float) args.get(1);

        var newY = (float) ((mtc.y - y) * Math.pow(0.5f, getLastFrameDuration()) + y);

        args.set(1, (float) Math.round(newY));
        mtc = new Vec2f(x, newY);
    }

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 7)
    private int mask(int m) {

        var shownLineCount = 0;
        for (int r = 0; r + scrolledLines < visibleMessages.size() && r < getVisibleLineCount(); r++) {
            if (savedCurrentTick - visibleMessages.get(r).addedTime() < 200 || isChatFocused()) shownLineCount++;
        }
        var targetHeight = shownLineCount * getLineHeight();

        maskHeightBuffer = (float) ((maskHeightBuffer - targetHeight) * Math.pow(0.5f, getLastFrameDuration()) + targetHeight);

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
        return (m);
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 14)
    private int opacity(int t) {
        return (0);
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 18)
    private int changePosY(int y) {
        return (y - getChatDrawOffset());
    }

    @ModifyVariable(method = "render", at = @At("STORE"))
    private long demask(long a) { // I don't why it throws error in ide but works fine after compile
        savedContext.disableScissor();
        return (a);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void renderT(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        scrolledLines = scrollValBefore;
    }

    @ModifyVariable(method = "addVisibleMessage", at = @At("STORE"), ordinal = 0)
    List<OrderedText> onNewMessage(List<OrderedText> ot) {
        if (refreshing) return (ot);
        scrollOffset -= ot.size() * getLineHeight();
        return (ot);
    }

    @Inject(method = "scroll", at = @At("HEAD"))
    public void scrollH(int scroll, CallbackInfo ci) {
        scrollValBefore = scrolledLines;
    }

    @Inject(method = "scroll", at = @At("TAIL"))
    public void scrollT(int scroll, CallbackInfo ci) {
        scrollOffset += (scrolledLines - scrollValBefore) * getLineHeight();
    }

    @Inject(method = "resetScroll", at = @At("HEAD"))
    public void scrollResetH(CallbackInfo ci) {
        scrollValBefore = scrolledLines;
    }

    @Inject(method = "resetScroll", at = @At("TAIL"))
    public void scrollResetT(CallbackInfo ci) {
        scrollOffset += (scrolledLines - scrollValBefore) * getLineHeight();
    }

    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;getLineHeight()I"), ordinal = 3)
    private int addLinesAbove(int i) {
        return ((int) Math.ceil(Math.round(maskHeightBuffer) / (float) getLineHeight()) + (getChatScrollOffset() < 0 ? 1 : 0));
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 12)
    private int addLinesUnder(int r) {
        if (scrolledLines == 0 || getChatScrollOffset() <= 0) return (r);
        return (r - 1);
    }

    @Inject(method = "refresh", at = @At("HEAD"))
    private void refreshH(CallbackInfo ci) {
        refreshing = true;
    }

    @Inject(method = "refresh", at = @At("TAIL"))
    private void refreshT(CallbackInfo ci) {
        refreshing = false;
    }

    @Shadow
    private int getLineHeight() {
        return (0);
    }

    @Shadow
    public int getVisibleLineCount() {
        return (0);
    }

    @Shadow
    public boolean isChatFocused() {
        return (false);
    }

    @Unique
    int getChatDrawOffset() {
        return Math.round(scrollOffset) - (Math.round(scrollOffset) / getLineHeight() * getLineHeight());
    }

    @Unique
    int getChatScrollOffset() {
        return Math.round(scrollOffset);
    }

    @Unique
    public float getLastFrameDuration() {
        return MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration();
    }
}
