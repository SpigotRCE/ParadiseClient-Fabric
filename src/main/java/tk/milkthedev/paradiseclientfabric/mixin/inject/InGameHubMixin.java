package tk.milkthedev.paradiseclientfabric.mixin.inject;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static tk.milkthedev.paradiseclientfabric.Helper.getChroma;

@Mixin(InGameHud.class)
public abstract class InGameHubMixin
{
    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Inject(method = "render", at = @At("TAIL"))
    public void renderMainHud(DrawContext context, float tickDelta, CallbackInfo info)
    {
        String title = "ParadiseClient by SpigotRCE#0";
        char[] chars = title.toCharArray();
        int i = 0;
        for (char aChar : chars)
        {
            String c = String.valueOf(aChar);
            context.drawText(this.client.textRenderer, c, 5 + i, 5, getChroma(i * -17, 1, 1).getRGB(), false);
            i += getTextRenderer().getWidth(c);
        }
    }
}
