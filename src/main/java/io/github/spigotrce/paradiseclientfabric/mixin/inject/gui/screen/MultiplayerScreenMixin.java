package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui.screen;

import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.mod.BungeeSpoofMod;
import io.github.spigotrce.paradiseclientfabric.screen.UUIDSpoofScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.AddServerScreen;
import net.minecraft.client.gui.screen.multiplayer.DirectConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.network.LanServerQueryManager;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;

/**
 * Mixin for the MultiplayerScreen class to add custom GUI elements.
 * This mixin injects additional buttons and text fields into the multiplayer settings screen.
 *
 * @author SpigotRCE
 * @since 1.0
 */
@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

    @Shadow
    @Final
    private static Logger LOGGER;
    /**
     * Reference to the BungeeSpoofMod instance for accessing mod data.
     */
    @Unique
    final BungeeSpoofMod bungeeSpoofMod = ParadiseClient_Fabric.BUNGEE_SPOOF_MOD;
    @Shadow
    protected MultiplayerServerListWidget serverListWidget;
    @Unique
    ButtonWidget uuidSpoofButton;
    /**
     * Button for toggling BungeeCord spoofing.
     */
    @Unique
    ButtonWidget bungeeToggleButton;
    /**
     * Text field for inputting BungeeCord IP.
     */
    @Unique
    TextFieldWidget bungeeClientIPField;
    /**
     * Button for toggling BungeeCord target hostname spoofing.
     */
    @Unique
    ButtonWidget bungeeHostnameToggle;
    /**
     * Text field for inputting BungeeCord target hostname.
     */
    @Unique
    TextFieldWidget bungeeHostnameField;
    /**
     * Renderer for displaying text.
     */
    @Unique
    TextRenderer textRenderer;
    @Shadow
    private boolean initialized;
    @Shadow
    private ServerList serverList;
    @Shadow
    private LanServerQueryManager.LanServerEntryList lanServers;
    @Shadow
    @Nullable
    private LanServerQueryManager.LanServerDetector lanServerDetector;
    @Shadow
    private ButtonWidget buttonJoin;
    @Shadow
    private ServerInfo selectedEntry;
    @Shadow
    private ButtonWidget buttonEdit;
    @Shadow
    private ButtonWidget buttonDelete;

    /**
     * Constructor for MultiplayerScreenMixin.
     *
     * @param title The title of the screen.
     */
    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract void directConnect(boolean confirmedAction);

    @Shadow
    public abstract void connect();

    @Shadow
    protected abstract void addEntry(boolean confirmedAction);

    @Shadow
    protected abstract void editEntry(boolean confirmedAction);

    @Shadow
    protected abstract void removeEntry(boolean confirmedAction);

    @Shadow
    protected abstract void updateButtonActivationStates();

    @Shadow
    protected abstract void refresh();

    /**
     * @author a
     * @reason a
     */
    @Overwrite
    public void init() {
        if (this.client == null) return; // To shut Intellij up

        if (this.initialized) {
            this.serverListWidget.setDimensionsAndPosition(this.width, this.height - 64 - 32, 0, 32);
        } else {
            this.initialized = true;
            this.serverList = new ServerList(this.client);
            this.serverList.loadFile();
            this.lanServers = new LanServerQueryManager.LanServerEntryList();

            try {
                this.lanServerDetector = new LanServerQueryManager.LanServerDetector(this.lanServers);
                this.lanServerDetector.start();
            } catch (Exception exception) {
                LOGGER.warn("Unable to start LAN server detection: {}", exception.getMessage());
            }

            this.serverListWidget = new MultiplayerServerListWidget(
                    (MultiplayerScreen) MinecraftClient.getInstance().currentScreen,
                    this.client,
                    this.width,
                    this.height - 64 - 32,
                    32,
                    36
            );
            this.serverListWidget.setServers(this.serverList);
        }

        this.addDrawableChild(this.serverListWidget);

        this.textRenderer = MinecraftClient.getInstance().textRenderer;


        this.uuidSpoofButton = this.addDrawableChild(
                ButtonWidget.builder(Text.literal("UUIDSpoof"),
                                onPress -> MinecraftClient.getInstance().setScreen(new UUIDSpoofScreen(this)))
                        .width(100)
                        .build()
        );

        this.bungeeToggleButton = this.addDrawableChild(
                ButtonWidget.builder(getBungeeButtonText(),
                                onPress -> {
                                    this.bungeeSpoofMod.isIPForwarding = !bungeeSpoofMod.isIPForwarding;
                                    this.bungeeToggleButton.setMessage(getBungeeButtonText());
                                })
                        .width(100)
                        .build()
        );

        this.bungeeHostnameToggle = this.addDrawableChild(
                ButtonWidget.builder(getBungeeTargetButtonText(),
                                onPress -> {
                                    this.bungeeSpoofMod.isHostnameForwarding = !bungeeSpoofMod.isHostnameForwarding;
                                    this.bungeeHostnameToggle.setMessage(getBungeeTargetButtonText());
                                })
                        .width(100)
                        .build()
        );

        this.bungeeClientIPField = new TextFieldWidget(this.textRenderer, 74, 20, Text.literal("Bungee IP"));
        this.bungeeClientIPField.setMaxLength(128);
        this.bungeeClientIPField.setText(bungeeSpoofMod.ip);
        this.bungeeClientIPField.setChangedListener((text) -> bungeeSpoofMod.ip = this.bungeeClientIPField.getText());
        this.addSelectableChild(this.bungeeClientIPField);

        this.bungeeHostnameField = new TextFieldWidget(this.textRenderer, 74, 20, Text.literal("Hostname"));
        this.bungeeHostnameField.setMaxLength(128);
        this.bungeeHostnameField.setText(bungeeSpoofMod.hostname);
        this.bungeeHostnameField.setChangedListener((text) -> bungeeSpoofMod.hostname = this.bungeeHostnameField.getText());
        this.addSelectableChild(this.bungeeHostnameField);

        this.buttonJoin = this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("selectServer.select"),
                                (button) -> this.connect())
                        .width(100)
                        .build()
        );

        ButtonWidget buttonWidget = this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("selectServer.direct"),
                                (button) -> {
                                    this.selectedEntry = new ServerInfo(I18n.translate("selectServer.defaultName"), "", ServerInfo.ServerType.OTHER);
                                    this.client.setScreen(new DirectConnectScreen(this, this::directConnect, this.selectedEntry));
                                })
                        .width(100)
                        .build()
        );

        ButtonWidget buttonWidget2 = this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("selectServer.add"),
                                (button) -> {
                                    this.selectedEntry = new ServerInfo(I18n.translate("selectServer.defaultName"), "", ServerInfo.ServerType.OTHER);
                                    this.client.setScreen(new AddServerScreen(this, this::addEntry, this.selectedEntry));
                                })
                        .width(100)
                        .build()
        );

        this.buttonEdit = this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("selectServer.edit"),
                                (button) -> {
                                    MultiplayerServerListWidget.Entry entry = this.serverListWidget.getSelectedOrNull();
                                    if (entry instanceof MultiplayerServerListWidget.ServerEntry) {
                                        ServerInfo serverInfo = ((MultiplayerServerListWidget.ServerEntry) entry).getServer();
                                        this.selectedEntry = new ServerInfo(serverInfo.name, serverInfo.address, ServerInfo.ServerType.OTHER);
                                        this.selectedEntry.copyWithSettingsFrom(serverInfo);
                                        this.client.setScreen(new AddServerScreen(this, this::editEntry, this.selectedEntry));
                                    }
                                })
                        .width(74)
                        .build()
        );

        this.buttonDelete = this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("selectServer.delete"),
                                (button) -> {
                                    MultiplayerServerListWidget.Entry entry = this.serverListWidget.getSelectedOrNull();
                                    if (entry instanceof MultiplayerServerListWidget.ServerEntry) {
                                        String string = ((MultiplayerServerListWidget.ServerEntry) entry).getServer().name;
                                        if (string != null) {
                                            Text text = Text.translatable("selectServer.deleteQuestion");
                                            Text text2 = Text.translatable("selectServer.deleteWarning", string);
                                            Text text3 = Text.translatable("selectServer.deleteButton");
                                            Text text4 = ScreenTexts.CANCEL;
                                            this.client.setScreen(new ConfirmScreen(this::removeEntry, text, text2, text3, text4));
                                        }
                                    }
                                })
                        .width(74)
                        .build()
        );

        ButtonWidget buttonWidget3 = this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("selectServer.refresh"),
                                (button) -> this.refresh())
                        .width(74)
                        .build()
        );

        ButtonWidget buttonWidget4 = this.addDrawableChild(
                ButtonWidget.builder(ScreenTexts.BACK,
                                (button) -> this.close())
                        .width(74)
                        .build()
        );

        DirectionalLayoutWidget directionalLayoutWidget = DirectionalLayoutWidget.vertical();

        AxisGridWidget axisGridWidget = directionalLayoutWidget.add(
                new AxisGridWidget(550, 20, AxisGridWidget.DisplayAxis.HORIZONTAL)
        );
        axisGridWidget.add(this.uuidSpoofButton);
        axisGridWidget.add(this.buttonJoin);
        axisGridWidget.add(buttonWidget);
        axisGridWidget.add(buttonWidget2);
        axisGridWidget.add(this.bungeeToggleButton);

        directionalLayoutWidget.add(EmptyWidget.ofHeight(4));

        AxisGridWidget axisGridWidget2 = directionalLayoutWidget.add(
                new AxisGridWidget(550, 20, AxisGridWidget.DisplayAxis.HORIZONTAL)
        );
        axisGridWidget2.add(this.bungeeClientIPField);
        axisGridWidget2.add(this.buttonEdit);
        axisGridWidget2.add(this.bungeeHostnameToggle);
        axisGridWidget2.add(this.buttonDelete);
        axisGridWidget2.add(buttonWidget3);
        axisGridWidget2.add(buttonWidget4);
        axisGridWidget2.add(this.bungeeHostnameField);

        directionalLayoutWidget.refreshPositions();
        SimplePositioningWidget.setPos(
                directionalLayoutWidget,
                0,
                this.height - 64,
                this.width,
                64
        );

        this.addDrawable(this.bungeeClientIPField); // right now, minecraft fucks up adding TextFieldWidget to AxisGridWidget
        this.addDrawable(this.bungeeHostnameField); // same as line 313

        this.updateButtonActivationStates();
    }

    /**
     * Gets the text for the BungeeCord toggle button based on its current state.
     *
     * @return The text to display on the BungeeCord button.
     */
    @Unique
    private Text getBungeeButtonText() {
        return bungeeSpoofMod.isIPForwarding ? Text.literal("Bungee Enabled") : Text.literal("Bungee Disabled");
    }

    /**
     * Gets the text for the BungeeCord target hostname toggle button based on its current state.
     *
     * @return The text to display on the BungeeCord target hostname button.
     */
    @Unique
    private Text getBungeeTargetButtonText() {
        return bungeeSpoofMod.isHostnameForwarding ? Text.literal("Hostname Enabled") : Text.literal("Hostname Disabled");
    }
}
