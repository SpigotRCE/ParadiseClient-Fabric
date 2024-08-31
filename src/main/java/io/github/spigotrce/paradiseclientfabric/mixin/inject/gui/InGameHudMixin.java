package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui;

import io.github.spigotrce.paradiseclientfabric.Constants;
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
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.mod.MiscMod;

import java.util.ArrayList;
import java.util.Objects;

import static io.github.spigotrce.paradiseclientfabric.Helper.getChroma;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Unique
    MiscMod miscMod = ParadiseClient_Fabric.getMiscMod();
    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MinecraftClient client, CallbackInfo ci) {
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (this.client == null) {
            return;
        }

        ArrayList<String> text = new ArrayList<>();

        text.add("ParadiseClient [" + Constants.EDITION + "]" + " by SpigotRCE#0");
        text.add("Server " + ((!Objects.isNull(this.client.getCurrentServerEntry()) && ParadiseClient_Fabric.getHudMod().showServerIP) ? this.client.getCurrentServerEntry().address : "Hidden"));
        text.add("Engine " + (Objects.isNull(this.client.player.networkHandler) ? "" : this.client.player.networkHandler.getBrand()));
        text.add("Last Incoming Packet " + (System.currentTimeMillis() - miscMod.lastIncomingPacketTime) + "ms Average " + miscMod.averageIncomingPacketDelay + "ms");
        text.add("Packet " + miscMod.lastIncomingPacket.getPacketId().id());
        text.add("Last Outgoing Packet " + (System.currentTimeMillis() - miscMod.lastOutgoingPacketTime) + "ms Average " + miscMod.averageOutgoingPacketDelay + "ms");
        text.add("Packet " + miscMod.lastOutgoingPacket.getPacketId().id());
        text.add("FPS " + this.client.getCurrentFps());
        text.add("Players: " + this.client.player.networkHandler.getPlayerList().size());

        int i = 0;
        for (String s : text) {
            renderTextWithChroma(context, s, 5, 5 + this.client.textRenderer.fontHeight * i);
            i++;
        }
    }

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
