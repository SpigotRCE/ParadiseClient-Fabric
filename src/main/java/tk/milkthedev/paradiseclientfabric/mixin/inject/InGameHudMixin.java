package tk.milkthedev.paradiseclientfabric.mixin.inject;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tk.milkthedev.paradiseclientfabric.mod.MiscMod;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Objects;

import static tk.milkthedev.paradiseclientfabric.Helper.getChroma;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin
{
    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Unique
    ClientPlayNetworkHandler clientPlayNetworkHandler;

    @Unique
    MiscMod miscMod = ParadiseClient_Fabric.getMiscMod();

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MinecraftClient client, CallbackInfo ci)
    {
        this.clientPlayNetworkHandler = this.client.getNetworkHandler();
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci)
    {
        if (this.client == null)
        {
            return;
        }

        ArrayList<String> text = new ArrayList<>();

        text.add("ParadiseClient by SpigotRCE#0");
        text.add("Server " + ((!Objects.isNull(this.client.getCurrentServerEntry()) && ParadiseClient_Fabric.getHudMod().showServerIP) ? this.client.getCurrentServerEntry().address : "Hidden"));
        text.add("Engine " + (Objects.isNull(this.client.getNetworkHandler()) ? "" : this.client.getNetworkHandler().getBrand()));
        text.add("Last Incoming Packet " + (System.currentTimeMillis() - miscMod.lastIncomingPacketTime) + "ms Average " + miscMod.averageIncomingPacketDelay + "ms");
        text.add("Packet " + miscMod.lastIncomingPacket.getPacketId().id());
        text.add("Last Outgoing Packet " + (System.currentTimeMillis() - miscMod.lastOutgoingPacketTime) + "ms Average " + miscMod.averageOutgoingPacketDelay + "ms");
        text.add("Packet " + miscMod.lastOutgoingPacket.getPacketId().id());
        text.add("FPS " + this.client.getCurrentFps());

        int i = 0;
        for (String s : text)
        {
            renderTextWithChroma(context, s, 5, 5 + this.client.textRenderer.fontHeight * i);
            i++;
        }
    }

    @Unique
    private void renderTextWithChroma(DrawContext ct, String s, int x, int y)
    {
        char[] chars = s.toCharArray();
        int i = 0;
        for (char aChar : chars)
        {
            String c = String.valueOf(aChar);
            ct.drawText(this.client.textRenderer, c, x + i, y, getChroma(((int) Math.sqrt(x * x + y * y) * 10) + (i * -17), 1, 1).getRGB(), false);
            i += getTextRenderer().getWidth(c);
        }
    }
}
