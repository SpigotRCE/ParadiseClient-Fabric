package tk.milkthedev.paradiseclientfabric.mixin.inject;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tk.milkthedev.paradiseclientfabric.BungeeSpoofMod;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;
import tk.milkthedev.paradiseclientfabric.screen.UUIDSpoofScreen;
@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen
{

    @Shadow protected abstract void init();

    @Unique
    BungeeSpoofMod bungeeSpoofMod = ParadiseClient_Fabric.getBungeeSpoofMod();

    @Unique
    ButtonWidget bungeeButton;

    @Unique
    TextFieldWidget bungeeIPButton;

    @Unique
    TextRenderer textRenderer;

    protected MultiplayerScreenMixin(Text title) {super(title);}

    @Inject(method = "init", at = @At(value = "TAIL", target = "Lnet/minecraft/client/gui/screen/multiplayer/MultiplayerScreen;updateButtonActivationStates()V"))
    private void init(CallbackInfo info)
    {
        this.textRenderer = MinecraftClient.getInstance().textRenderer;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("UUIDSpoof"),
                        onPress -> MinecraftClient.getInstance().setScreen(new UUIDSpoofScreen(this)))
                .width(80)
                .position(5, this.height - 96)
                .build()
        );

        this.bungeeButton = this.addDrawableChild(ButtonWidget.builder(getBungeeButtonText(),
                        onPress -> onBungeeButtonPress())
                .width(100)
                .position(5, this.height - 128)
                .build()
        );

        this.bungeeIPButton = new TextFieldWidget(this.textRenderer, 5, this.height - 160, 50, 20, Text.literal("Bungee IP")); // Set width to 150 and height to 20
        this.bungeeIPButton.setMaxLength(128);
        this.bungeeIPButton.setText(bungeeSpoofMod.getBungeeIP());
        this.bungeeIPButton.setChangedListener((text) -> this.onBungeeIPButtonPress());
        this.addSelectableChild(this.bungeeIPButton);
        this.addDrawable(this.bungeeIPButton);
    }

    @Unique
    private void onBungeeButtonPress()
    {
        this.bungeeSpoofMod.setBungeeEnabled(!bungeeSpoofMod.isBungeeEnabled());
        this.bungeeButton.setMessage(getBungeeButtonText());
    }

    @Unique
    private Text getBungeeButtonText() {return bungeeSpoofMod.isBungeeEnabled()? Text.literal("Bungee Enabled") : Text.literal("Bungee Disabled");}

    @Unique
    private void onBungeeIPButtonPress()
    {
        bungeeSpoofMod.setBungeeIP(this.bungeeIPButton.getText());
    }
}