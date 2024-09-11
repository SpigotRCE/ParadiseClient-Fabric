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

/**
 * Mixin class to customize the behavior of the Title Screen in Minecraft.
 * <p>
 * This class modifies the Title Screen to include a custom button recommending
 * the installation of "ViaFabricPlus" and customizes the background fade effect.
 * It also displays additional information about the client and game version.
 *
 * @author SpigotRCE
 * @since 2.9
 * </p>
 */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    /**
     * Message recommending the installation of "ViaFabricPlus".
     */
    @Unique
    private final String VIAFABRICPLUS_REMINDER = "We recommend installing ViaFabricPlus";

    /**
     * The splash text renderer used to display splash texts on the Title Screen.
     */
    @Nullable
    @Shadow
    private SplashTextRenderer splashText;

    /**
     * The Realms Notifications Screen displayed on the Title Screen if active.
     */
    @Nullable
    @Shadow
    private RealmsNotificationsScreen realmsNotificationGui;

    /**
     * Alpha value for the background fade effect on the Title Screen.
     */
    @Mutable
    @Shadow
    private float backgroundAlpha;

    /**
     * Flag indicating whether the background fade effect is enabled.
     */
    @Mutable
    @Shadow
    private boolean doBackgroundFade;

    /**
     * The start time for the background fade effect, in milliseconds.
     */
    @Mutable
    @Shadow
    private long backgroundFadeStart;

    /**
     * The logo drawer used to render the logo on the Title Screen.
     */
    @Final
    @Shadow
    private LogoDrawer logoDrawer;

    /**
     * Constructs a new instance of the TitleScreenMixin.
     *
     * @param title The title of the screen.
     */
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    /**
     * Injects a custom button into the Title Screen if "viafabricplus" is not loaded.
     * The button directs the user to a URL for installation.
     *
     * @param ci Callback information.
     */
    @Inject(method = "init", at = @At(value = "TAIL"))
    public void init(CallbackInfo ci) {
        if (!FabricLoader.getInstance().isModLoaded("viafabricplus")) {
            this.addDrawableChild(ButtonWidget.builder(Text.literal(VIAFABRICPLUS_REMINDER),
                            onPress -> {
                                Util.getOperatingSystem().open("https://modrinth.com/mod/viafabricplus/version/3.4.2");
                                MinecraftClient.getInstance().setScreen(new TitleScreen());
                            })
                    .width(this.textRenderer.getWidth(VIAFABRICPLUS_REMINDER) + 5)
                    .position((this.width / 2) - ((this.textRenderer.getWidth(VIAFABRICPLUS_REMINDER) + 5) / 2), this.height / 4 + 48 + 72 + 12 + 35)
                    .build()
            );
        }
    }

    /**
     * Checks if the Realms Notifications GUI is displayed.
     * This method is shadowed from the original TitleScreen class.
     *
     * @return True if the Realms Notifications GUI is displayed, false otherwise.
     */
    @Shadow
    private boolean isRealmsNotificationsGuiDisplayed() {
        return false;
    }

    /**
     * Sets the alpha value for widgets.
     * This method is shadowed from the original TitleScreen class.
     *
     * @param alpha The alpha value to set.
     */
    @Shadow
    private void setWidgetAlpha(float alpha) {
    }

    /**
     * Renders the Title Screen with custom background and additional information.
     * This method handles background fading and custom text rendering.
     *
     * @param context The draw context used for rendering.
     * @param mouseX  The mouse X position.
     * @param mouseY  The mouse Y position.
     * @param delta   The delta time since the last frame.
     * @param ci      Callback information.
     */
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.backgroundFadeStart == 0L && this.doBackgroundFade) {
            this.backgroundFadeStart = Util.getMeasuringTimeMs();
        }

        float f = 1.0F;
        if (this.doBackgroundFade) {
            float g = (float) (Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 2000.0F;
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
            if (this.splashText != null && !(Boolean) this.client.options.getHideSplashTexts().getValue()) {
                this.splashText.render(context, this.width, this.textRenderer, i);
            }

            String string = "ParadiseClient [" + Constants.EDITION + "]" + Constants.VERSION + "/" + SharedConstants.getGameVersion().getName();
            context.drawTextWithShadow(this.textRenderer, string, 2, this.height - 10, 16777215 | i);
            if (this.isRealmsNotificationsGuiDisplayed() && f >= 1.0F) {
                RenderSystem.enableDepthTest();
                this.realmsNotificationGui.render(context, mouseX, mouseY, delta);
            }
        }

        ci.cancel();
    }
}
