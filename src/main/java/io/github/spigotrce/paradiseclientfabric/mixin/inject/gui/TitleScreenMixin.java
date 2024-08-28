package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Unique
    String VIAFABRICPLUS_REMINDER = "We recommend to install ViaFabricPlus";

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
}
