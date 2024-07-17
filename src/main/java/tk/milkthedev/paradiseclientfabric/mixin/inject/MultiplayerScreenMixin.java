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
import tk.milkthedev.paradiseclientfabric.mod.BungeeSpoofMod;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;
import tk.milkthedev.paradiseclientfabric.screen.UUIDSpoofScreen;
@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

    @Shadow
    protected abstract void init();

    @Unique
    BungeeSpoofMod bungeeSpoofMod = ParadiseClient_Fabric.getBungeeSpoofMod();

    @Unique
    ButtonWidget bungeeButton;

    @Unique
    TextFieldWidget bungeeIPButton;

    @Unique
    ButtonWidget bungeeTargetButton;

    @Unique
    TextFieldWidget bungeeTargetIPButton;

    @Unique
    TextRenderer textRenderer;

    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At(value = "TAIL", target = "Lnet/minecraft/client/gui/screen/multiplayer/MultiplayerScreen;updateButtonActivationStates()V"))
    private void init(CallbackInfo info) {
        this.textRenderer = MinecraftClient.getInstance().textRenderer;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("UUIDSpoof"),
                        onPress -> MinecraftClient.getInstance().setScreen(new UUIDSpoofScreen(this)))
                .width(80)
                .position(5, this.height - 96)
                .build()
        );

        this.bungeeButton = this.addDrawableChild(ButtonWidget.builder(getBungeeButtonText(),
                        onPress -> {
                            this.bungeeSpoofMod.setBungeeEnabled(!bungeeSpoofMod.isBungeeEnabled());
                            this.bungeeButton.setMessage(getBungeeButtonText());
                        })
                .width(100)
                .position(5, this.height - 128)
                .build()
        );

        this.bungeeIPButton = new TextFieldWidget(this.textRenderer, 5, this.height - 160, 50, 20, Text.literal("Bungee IP")); // Set width to 150 and height to 20
        this.bungeeIPButton.setMaxLength(128);
        this.bungeeIPButton.setText(bungeeSpoofMod.getBungeeIP());
        this.bungeeIPButton.setChangedListener((text) -> bungeeSpoofMod.setBungeeIP(this.bungeeIPButton.getText()));
        this.addSelectableChild(this.bungeeIPButton);
        this.addDrawable(this.bungeeIPButton);

        this.bungeeTargetButton = this.addDrawableChild(ButtonWidget.builder(getBungeeTargetButtonText(),
                        onPress -> {
                            this.bungeeSpoofMod.setBungeeTargetEnabled(!bungeeSpoofMod.isBungeeTargetEnabled());
                            this.bungeeTargetButton.setMessage(getBungeeTargetButtonText());
                        })
                .width(100)
                .position(5, this.height - 192)
                .build()
        );

        this.bungeeTargetIPButton = new TextFieldWidget(this.textRenderer, 5, this.height - 224, 50, 20, Text.literal("Hostname")); // Set width to 150 and height to 20
        this.bungeeTargetIPButton.setMaxLength(128);
        this.bungeeTargetIPButton.setText(bungeeSpoofMod.getBungeeTargetIP());
        this.bungeeTargetIPButton.setChangedListener((text) -> bungeeSpoofMod.setTargetIP(this.bungeeTargetIPButton.getText()));
        this.addSelectableChild(this.bungeeTargetIPButton);
        this.addDrawable(this.bungeeTargetIPButton);

    }

    @Unique
    private Text getBungeeButtonText() {
        return bungeeSpoofMod.isBungeeEnabled() ? Text.literal("Bungee Enabled") : Text.literal("Bungee Disabled");
    }

    @Unique
    private Text getBungeeTargetButtonText() {
        return bungeeSpoofMod.isBungeeTargetEnabled() ? Text.literal("Hostname Enabled") : Text.literal("Hostname Disabled");
    }
}