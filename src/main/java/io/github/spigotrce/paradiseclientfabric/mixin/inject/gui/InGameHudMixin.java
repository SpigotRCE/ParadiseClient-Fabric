package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui;

import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Objects;

import static io.github.spigotrce.paradiseclientfabric.Helper.getChroma;

/**
 * Mixin for the InGameHud class to inject custom HUD rendering behavior.
 * This mixin is used to display additional information on the HUD.
 *
 * @author SpigotRCE
 * @since 1.0
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    /**
     * The Minecraft client instance.
     */
    @Final
    @Shadow
    private MinecraftClient client;
    @Shadow
    @Final
    private PlayerListHud playerListHud;

    /**
     * Gets the TextRenderer instance used for rendering text.
     *
     * @return The TextRenderer instance.
     */
    @Shadow
    public abstract TextRenderer getTextRenderer();

    /**
     * Injects behavior at the end of the InGameHud constructor.
     *
     * @param client The Minecraft client instance.
     * @param ci     Callback information for the method.
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MinecraftClient client, CallbackInfo ci) {
    }

    /**
     * Injects behavior at the end of the render method to add custom HUD information.
     *
     * @param context     The DrawContext used for rendering.
     * @param tickCounter The RenderTickCounter for frame timing.
     * @param ci          Callback information for the method.
     */
    @Inject(method = "render", at = @At("TAIL"))
    public void renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (this.client == null) {
            return;
        }

        ArrayList<String> text = new ArrayList<>();

        text.add(Constants.WINDOW_TITLE);
        text.add("Server " + ((!Objects.isNull(this.client.getCurrentServerEntry()) && ParadiseClient_Fabric.hudMod.showServerIP) ? this.client.getCurrentServerEntry().address : "Hidden"));
        assert this.client.player != null;
        text.add("Engine " + (Objects.isNull(this.client.player.networkHandler) ? "" : this.client.player.networkHandler.getBrand()));
        text.add("FPS " + this.client.getCurrentFps());
        text.add("Players: " + this.client.player.networkHandler.getPlayerList().size());

        int i = 0;
        for (String s : text) {
            renderTextWithChroma(context, s, 5, 5 + this.client.textRenderer.fontHeight * i);
            i++;
        }
    }

    /**
     * Renders text with a chroma color effect.
     *
     * @param ct The DrawContext used for rendering.
     * @param s  The string to render.
     * @param x  The x-coordinate for the text.
     * @param y  The y-coordinate for the text.
     */
    @SuppressWarnings("SameParameterValue")
    @Unique
    private void renderTextWithChroma(DrawContext ct, String s, int x, int y) {
        char[] chars = s.toCharArray();
        int i = 0;
        for (char aChar : chars) {
            String c = String.valueOf(aChar);
            ct.drawText(this.client.textRenderer, c, x + i, y, getChroma(((int) Math.sqrt(x * x + y * y) * 10) + (i * -17), 1, 1).getRGB(), false);
            i += getTextRenderer().getWidth(c);
        }
    }

    @Inject(method = "renderPlayerList", at = @At("HEAD"), cancellable = true)
    private void renderPlayerList(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        assert this.client.world != null;
        Scoreboard scoreboard = this.client.world.getScoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.LIST);
        if (!this.client.options.playerListKey.isPressed() || this.client.isInSingleplayer() && Objects.requireNonNull(this.client.player).networkHandler.getListedPlayerListEntries().size() <= 1 && scoreboardObjective == null) {
            this.playerListHud.setVisible(false);
            if (ParadiseClient_Fabric.hudMod.showPlayerList) {
                this.renderTAB(context, context.getScaledWindowWidth(), scoreboard, scoreboardObjective);
            }
        } else {
            this.renderTAB(context, context.getScaledWindowWidth(), scoreboard, scoreboardObjective);
        }
        ci.cancel();
    }

    @Unique
    private void renderTAB(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective scoreboardObjective) {
        this.playerListHud.setVisible(true);
        this.playerListHud.render(context, scaledWindowWidth, scoreboard, scoreboardObjective);
    }
}
