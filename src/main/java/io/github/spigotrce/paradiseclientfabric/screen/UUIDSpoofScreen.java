package io.github.spigotrce.paradiseclientfabric.screen;

import com.google.gson.JsonParser;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.mod.BungeeSpoofMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;

import static io.github.spigotrce.paradiseclientfabric.Constants.LOGGER;

/**
 * Screen for spoofing UUIDs.
 * <p>
 * This screen allows users to spoof their UUID by setting a Bungee username and a fake username,
 * and choosing whether the UUID should be premium or cracked.
 * </p>
 *
 * @author SpigotRCE
 * @since 1.0
 */
public class UUIDSpoofScreen extends Screen {
    /**
     * The BungeeSpoofMod instance used to manage Bungee spoofing settings.
     */
    private final BungeeSpoofMod bungeeSpoofMod = ParadiseClient_Fabric.bungeeSpoofMod;

    /**
     * The parent screen to return to when this screen is closed.
     */
    private final Screen parentScreen;

    /**
     * The Minecraft client instance used to interact with the game.
     */
    private final MinecraftClient minecraftClient = MinecraftClient.getInstance();

    /**
     * The current status message displayed on the screen.
     */
    private String status;

    /**
     * Text field for entering the Bungee username.
     */
    private TextFieldWidget bungeeUsernameField;

    /**
     * Text field for entering the Bungee fake username.
     */
    private TextFieldWidget bungeeFakeUsernameField;

    /**
     * Text field for entering the Bungee token.
     */
    private TextFieldWidget bungeeTokenField;

    /**
     * Button to toggle between premium and cracked UUIDs.
     */
    private ButtonWidget premiumButton;

    private int currentHeight;

    /**
     * Creates a new UUIDSpoofScreen.
     *
     * @param parentScreen The screen to return to when this screen is closed.
     */
    public UUIDSpoofScreen(Screen parentScreen) {
        super(Text.literal("UUID Spoof"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        this.status = "Stand by";

        int widgetWidth = 200;
        int widgetXOffset = widgetWidth / 2;

        this.currentHeight = this.height / 2 - 90;

        int tHeight;
        tHeight = getNewHeight();
        this.bungeeUsernameField = new TextFieldWidget(this.textRenderer, this.width / 2 - widgetXOffset, tHeight, widgetWidth, 20, Text.literal("Username"));
        this.bungeeUsernameField.setMaxLength(128);
        this.bungeeUsernameField.setText(this.bungeeSpoofMod.getBungeeUsername());
        this.addSelectableChild(this.bungeeUsernameField);
        this.addDrawable(this.bungeeUsernameField);
        this.addDrawable(new TextWidget(this.width / 2 - widgetXOffset, tHeight - 15, widgetWidth, 20, Text.literal("Username"), this.textRenderer));

        tHeight = getNewHeight();
        this.bungeeFakeUsernameField = new TextFieldWidget(this.textRenderer, this.width / 2 - widgetXOffset, tHeight, widgetWidth, 20, Text.literal("FakeUsername"));
        this.bungeeFakeUsernameField.setMaxLength(128);
        this.bungeeFakeUsernameField.setText(this.bungeeSpoofMod.getBungeeFakeUsername());
        this.addSelectableChild(this.bungeeFakeUsernameField);
        this.addDrawable(this.bungeeFakeUsernameField);
        this.addDrawable(new TextWidget(this.width / 2 - widgetXOffset, tHeight - 15, widgetWidth, 20, Text.literal("FakeUsername"), this.textRenderer));

        tHeight = getNewHeight();
        this.bungeeTokenField = new TextFieldWidget(this.textRenderer, this.width / 2 - widgetXOffset, tHeight, widgetWidth, 20, Text.literal("BungeeGuard Token"));
        this.bungeeTokenField.setMaxLength(256);
        this.bungeeTokenField.setText(this.bungeeSpoofMod.getBungeeToken());
        this.addSelectableChild(this.bungeeTokenField);
        this.addDrawable(this.bungeeTokenField);
        this.addDrawable(new TextWidget(this.width / 2 - widgetXOffset, tHeight - 15, widgetWidth, 20, Text.literal("BungeeGuard Token"), this.textRenderer));

        premiumButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(bungeeSpoofMod.isBungeeUUIDPremium() ? "Premium" : "Cracked"), button -> {
                    bungeeSpoofMod.setBungeeUUIDPremium(!bungeeSpoofMod.isBungeeUUIDPremium());
                    premiumButton.setMessage(Text.literal(bungeeSpoofMod.isBungeeUUIDPremium() ? "Premium" : "Cracked"));
                })
                .dimensions(this.width / 2 - widgetXOffset, getNewHeight() - 10, widgetWidth, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Spoof"), button -> spoof())
                .dimensions(this.width / 2 - widgetXOffset, getNewHeight() - 20, widgetWidth, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Exit"), button -> close())
                .dimensions(this.width / 2 - widgetXOffset, getNewHeight() - 30, widgetWidth, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.status, this.width / 2, 20, 16777215);
    }

    @Override
    public void close() {
        minecraftClient.setScreen(parentScreen);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String s1 = this.bungeeUsernameField.getText();
        String s2 = this.bungeeFakeUsernameField.getText();
        this.init(client, width, height);
        this.bungeeUsernameField.setText(s1);
        this.bungeeFakeUsernameField.setText(s2);
    }

    /**
     * Spoofs the UUID based on the input fields.
     * <p>
     * This method sets the Bungee username and fake username, and determines if the UUID should be
     * premium or cracked. It fetches the premium UUID from Mojang's API if necessary, otherwise
     * generates a cracked UUID.
     * </p>
     */
    private void spoof() {
        this.bungeeSpoofMod.setBungeeUsername(this.bungeeUsernameField.getText());
        this.bungeeSpoofMod.setBungeeFakeUsername(this.bungeeFakeUsernameField.getText());
        this.bungeeSpoofMod.setBungeeToken(this.bungeeTokenField.getText());
        if (this.bungeeSpoofMod.isBungeeUUIDPremium()) {
            try {
                this.bungeeSpoofMod.setBungeeUUID(fetchUUID(this.bungeeSpoofMod.getBungeeFakeUsername()));
                this.status = "Successfully spoofed premium UUID of \"" + this.bungeeSpoofMod.getBungeeFakeUsername() + "\".";
            } catch (Exception e) {
                this.status = "Error fetching UUID, \"" + this.bungeeSpoofMod.getBungeeFakeUsername() + "\" may not be premium.";
                LOGGER.error(Arrays.toString(e.getStackTrace()));
            }
            return;
        }
        this.status = "Generating cracked UUID";
        this.bungeeSpoofMod.setBungeeUUID(UUID.nameUUIDFromBytes(("OfflinePlayer:" + bungeeSpoofMod.getBungeeFakeUsername()).getBytes()).toString().replace("-", ""));
        this.status = "Successfully spoofed cracked UUID of \"" + this.bungeeSpoofMod.getBungeeFakeUsername() + "\".";
    }

    /**
     * Fetches the UUID for a given username from Mojang's API.
     *
     * @param username The username to fetch the UUID for.
     * @return The UUID as a string.
     * @throws Exception If there is an error fetching the UUID.
     */
    public String fetchUUID(String username) throws Exception {
        String urlString = "https://api.mojang.com/users/profiles/minecraft/" + username;
        @SuppressWarnings("deprecation") URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return JsonParser.parseString(response.toString()).getAsJsonObject().get("id").getAsString();
        }
        throw new Exception();
    }

    private int getNewHeight() {
        this.currentHeight += 35;
        return this.currentHeight;
    }
}
