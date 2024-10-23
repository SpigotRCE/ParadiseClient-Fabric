package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui.screen;

import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.mod.BungeeSpoofMod;
import io.github.spigotrce.paradiseclientfabric.screen.UUIDSpoofScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for the MultiplayerScreen class to add custom GUI elements.
 * This mixin injects additional buttons and text fields into the multiplayer settings screen.
 *
 * @author SpigotRCE
 * @since 1.0
 */
@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

    /**
     * Reference to the BungeeSpoofMod instance for accessing mod data.
     */
    @Unique
    final BungeeSpoofMod bungeeSpoofMod = ParadiseClient_Fabric.getBungeeSpoofMod();

    /**
     * Button for toggling BungeeCord spoofing.
     */
    @Unique
    ButtonWidget bungeeButton;

    /**
     * Text field for inputting BungeeCord IP.
     */
    @Unique
    TextFieldWidget bungeeIPButton;

    /**
     * Button for toggling BungeeCord target hostname spoofing.
     */
    @Unique
    ButtonWidget bungeeTargetButton;

    /**
     * Text field for inputting BungeeCord target hostname.
     */
    @Unique
    TextFieldWidget bungeeTargetIPButton;

    /**
     * Renderer for displaying text.
     */
    @Unique
    TextRenderer textRenderer;

    /**
     * Constructor for MultiplayerScreenMixin.
     *
     * @param title The title of the screen.
     */
    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    /**
     * Initializes the screen with additional buttons and text fields.
     *
     * @param info Callback information for the method.
     */
    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo info) {
        this.textRenderer = MinecraftClient.getInstance().textRenderer;

        // Adds the UUIDSpoof button which opens the UUIDSpoofScreen.
        this.addDrawableChild(ButtonWidget.builder(Text.literal("UUIDSpoof"),
                        onPress -> MinecraftClient.getInstance().setScreen(new UUIDSpoofScreen(this)))
                .width(80)
                .position(5, this.height - 96)
                .build()
        );

        // Adds the BungeeCord toggle button.
        this.bungeeButton = this.addDrawableChild(ButtonWidget.builder(getBungeeButtonText(),
                        onPress -> {
                            this.bungeeSpoofMod.setBungeeEnabled(!bungeeSpoofMod.isBungeeEnabled());
                            this.bungeeButton.setMessage(getBungeeButtonText());
                        })
                .width(100)
                .position(5, this.height - 128)
                .build()
        );

        // Adds the BungeeCord IP text field.
        this.bungeeIPButton = new TextFieldWidget(this.textRenderer, 5, this.height - 160, 50, 20, Text.literal("Bungee IP"));
        this.bungeeIPButton.setMaxLength(128);
        this.bungeeIPButton.setText(bungeeSpoofMod.getBungeeIP());
        this.bungeeIPButton.setChangedListener((text) -> bungeeSpoofMod.setBungeeIP(this.bungeeIPButton.getText()));
        this.addSelectableChild(this.bungeeIPButton);
        this.addDrawable(this.bungeeIPButton);

        // Adds the BungeeCord target hostname toggle button.
        this.bungeeTargetButton = this.addDrawableChild(ButtonWidget.builder(getBungeeTargetButtonText(),
                        onPress -> {
                            this.bungeeSpoofMod.setBungeeTargetEnabled(!bungeeSpoofMod.isBungeeTargetEnabled());
                            this.bungeeTargetButton.setMessage(getBungeeTargetButtonText());
                        })
                .width(100)
                .position(5, this.height - 192)
                .build()
        );

        // Adds the BungeeCord target hostname text field.
        this.bungeeTargetIPButton = new TextFieldWidget(this.textRenderer, 5, this.height - 224, 50, 20, Text.literal("Hostname"));
        this.bungeeTargetIPButton.setMaxLength(128);
        this.bungeeTargetIPButton.setText(bungeeSpoofMod.getBungeeTargetIP());
        this.bungeeTargetIPButton.setChangedListener((text) -> bungeeSpoofMod.setTargetIP(this.bungeeTargetIPButton.getText()));
        this.addSelectableChild(this.bungeeTargetIPButton);
        this.addDrawable(this.bungeeTargetIPButton);
    }

    /**
     * Gets the text for the BungeeCord toggle button based on its current state.
     *
     * @return The text to display on the BungeeCord button.
     */
    @Unique
    private Text getBungeeButtonText() {
        return bungeeSpoofMod.isBungeeEnabled() ? Text.literal("Bungee Enabled") : Text.literal("Bungee Disabled");
    }

    /**
     * Gets the text for the BungeeCord target hostname toggle button based on its current state.
     *
     * @return The text to display on the BungeeCord target hostname button.
     */
    @Unique
    private Text getBungeeTargetButtonText() {
        return bungeeSpoofMod.isBungeeTargetEnabled() ? Text.literal("Hostname Enabled") : Text.literal("Hostname Disabled");
    }
}
