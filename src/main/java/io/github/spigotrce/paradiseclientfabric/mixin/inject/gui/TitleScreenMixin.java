package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.spigotrce.paradiseclientfabric.Constants;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsNotificationsScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Unique
    String VIAFABRICPLUS_REMINDER = "We recommend to install ViaFabricPlus";
    @Nullable
    @Shadow
    private SplashTextRenderer splashText;
    @Nullable
    @Shadow
    private RealmsNotificationsScreen realmsNotificationGui;
    @Mutable
    @Shadow
    private float backgroundAlpha;
    @Mutable
    @Shadow
    private boolean doBackgroundFade;
    @Mutable
    @Shadow
    private long backgroundFadeStart;
    @Final
    @Shadow
    private LogoDrawer logoDrawer;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    public void init(CallbackInfo ci) {
        if (!FabricLoader.getInstance().isModLoaded("viafabricplus"))
            this.addDrawableChild(ButtonWidget.builder(Text.literal(VIAFABRICPLUS_REMINDER),
                            onPress ->
                            {
                                Util.getOperatingSystem().open("https://modrinth.com/mod/viafabricplus/version/3.4.2");
                                MinecraftClient.getInstance().setScreen(new TitleScreen());
                            })
                    .width(this.textRenderer.getWidth(VIAFABRICPLUS_REMINDER) + 5)
                    .position((this.width / 2) - ((this.textRenderer.getWidth(VIAFABRICPLUS_REMINDER) + 5) / 2), this.height / 4 + 48 + 72 + 12 + 35)
                    .build()
            );
    }
    @Shadow
    private boolean isRealmsNotificationsGuiDisplayed() {
        return false;
    }

    @Shadow
    private void setWidgetAlpha(float alpha) {
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.backgroundFadeStart == 0L && this.doBackgroundFade) {
            this.backgroundFadeStart = Util.getMeasuringTimeMs();
        }

        float f = 1.0F;
        if (this.doBackgroundFade) {
            float g = (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 2000.0F;
            if (g > 1.0F) {
                this.doBackgroundFade = false;
                this.backgroundAlpha = 1.0F;
            } else {
                g = MathHelper.clamp(g, 0.0F, 1.0F);
                f = MathHelper.clampedMap(g, 0.5F, 1.0F, 0.0F, 1.0F);
                this.backgroundAlpha = MathHelper.clampedMap(g, 0.0F, 0.5F, 0.0F, 1.0F);
            }

            this.setWidgetAlpha(f);
        }

        this.renderPanoramaBackground(context, delta);
        int i = MathHelper.ceil(f * 255.0F) << 24;
        if ((i & -67108864) != 0) {
            super.render(context, mouseX, mouseY, delta);
            this.logoDrawer.draw(context, this.width, f);
            if (this.splashText != null && !(Boolean)this.client.options.getHideSplashTexts().getValue())
                this.splashText.render(context, this.width, this.textRenderer, i);


            String string = "ParadiseClient " + Constants.VERSION + "/" + SharedConstants.getGameVersion().getName();

            context.drawTextWithShadow(this.textRenderer, string, 2, this.height - 10, 16777215 | i);
            if (this.isRealmsNotificationsGuiDisplayed() && f >= 1.0F) {
                RenderSystem.enableDepthTest();
                this.realmsNotificationGui.render(context, mouseX, mouseY, delta);
            }
        }

        ci.cancel();
    }
}
