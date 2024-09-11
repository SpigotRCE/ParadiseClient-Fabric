package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui;

import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.mod.MiscMod;
import io.github.spigotrce.paradiseclientfabric.mod.NetworkMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
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
     * Reference to the MiscMod instance for accessing mod data.
     */
    @Unique
    MiscMod miscMod = ParadiseClient_Fabric.getMiscMod();

    /**
     * Reference to the MiscMod instance for accessing mod data.
     */
    @Unique
    NetworkMod networkMod = ParadiseClient_Fabric.getNetworkMod();

    /**
     * The Minecraft client instance.
     */
    @Final
    @Shadow
    private MinecraftClient client;

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

        text.add("ParadiseClient [" + Constants.EDITION + "] by SpigotRCE#0");
        text.add("Server " + ((!Objects.isNull(this.client.getCurrentServerEntry()) && ParadiseClient_Fabric.getHudMod().showServerIP) ? this.client.getCurrentServerEntry().address : "Hidden"));
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
}
