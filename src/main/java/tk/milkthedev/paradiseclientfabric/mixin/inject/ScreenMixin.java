package tk.milkthedev.paradiseclientfabric.mixin.inject;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tk.milkthedev.paradiseclientfabric.Constants;

@Mixin(Screen.class)
public abstract class ScreenMixin
{
    @Shadow public int height;
    @Shadow public int width;
    @Unique
    private final Identifier backgroundImage = Identifier.of(Constants.MOD_ID, "wallpaper.png");

    @Inject(method = "renderBackground", at = @At(value = "HEAD"), cancellable = true)
    private void renderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci)
    {
        if (MinecraftClient.getInstance().currentScreen instanceof MultiplayerScreen)
        {
            context.drawTexture(backgroundImage, 0, 0, this.width, this.height, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
            ci.cancel();
        }
    }
}
