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
 * This screen allows users to spoof their UUID by setting a Bungee username, a fake username,
 * and choosing between premium or cracked UUIDs.
 */
public class UUIDSpoofScreen extends Screen {

    private final BungeeSpoofMod bungeeSpoofMod = ParadiseClient_Fabric.bungeeSpoofMod;
    private final Screen parentScreen;
    private final MinecraftClient minecraftClient = MinecraftClient.getInstance();

    private String status;
    private TextFieldWidget bungeeUsernameField;
    private TextFieldWidget bungeeFakeUsernameField;
    private TextFieldWidget bungeeTokenField;
    private ButtonWidget premiumButton;
    private int currentHeight;

    public UUIDSpoofScreen(Screen parentScreen) {
        super(Text.literal("UUID Spoof"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        status = "Stand by";
        int widgetWidth = 200;
        int widgetXOffset = widgetWidth / 2;
        currentHeight = this.height / 2 - 90;

        addInputField("Username", this.bungeeSpoofMod.usernameReal, value -> this.bungeeSpoofMod.usernameReal = value);
        addInputField("FakeUsername", this.bungeeSpoofMod.usernameFake, value -> this.bungeeSpoofMod.usernameFake = value);
        addInputField("BungeeGuard Token", this.bungeeSpoofMod.token, value -> this.bungeeSpoofMod.token = value);

        premiumButton = addButton("Premium", widgetWidth, widgetXOffset, button -> togglePremium());
        addButton("Spoof", widgetWidth, widgetXOffset, button -> spoof());
        addButton("Exit", widgetWidth, widgetXOffset, button -> close());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.status, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public void close() {
        minecraftClient.setScreen(parentScreen);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String username = this.bungeeUsernameField.getText();
        String fakeUsername = this.bungeeFakeUsernameField.getText();
        String token = this.bungeeTokenField.getText();
        this.init(client, width, height);
        this.bungeeUsernameField.setText(username);
        this.bungeeFakeUsernameField.setText(fakeUsername);
        this.bungeeTokenField.setText(token);
    }

    private void spoof() {
        this.bungeeSpoofMod.sessionAccessor.paradiseClient_Fabric$setUsername(this.bungeeSpoofMod.usernameReal);
        if (this.bungeeSpoofMod.isUUIDOnline) {
            try {
                this.bungeeSpoofMod.uuid = fetchUUID(this.bungeeSpoofMod.usernameFake);
                this.status = "Successfully spoofed premium UUID for \"" + this.bungeeSpoofMod.usernameFake + "\".";
            } catch (Exception e) {
                this.status = "Error fetching UUID. \"" + this.bungeeSpoofMod.usernameFake + "\" may not be premium.";
                LOGGER.error("Error fetching UUID", e);
            }
        } else {
            this.status = "Generating cracked UUID";
            this.bungeeSpoofMod.uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + bungeeSpoofMod.usernameFake).getBytes()).toString().replace("-", "");
            this.status = "Successfully spoofed cracked UUID for \"" + this.bungeeSpoofMod.usernameFake + "\".";
        }
    }

    private String fetchUUID(String username) throws Exception {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() == 200) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String response = reader.lines().reduce("", (acc, line) -> acc + line);
                return JsonParser.parseString(response).getAsJsonObject().get("id").getAsString();
            }
        }
        throw new Exception("Failed to fetch UUID");
    }

    private void togglePremium() {
        bungeeSpoofMod.isUUIDOnline = !bungeeSpoofMod.isUUIDOnline;
        premiumButton.setMessage(Text.literal(bungeeSpoofMod.isUUIDOnline ? "Premium" : "Cracked"));
    }

    private int getNewHeight() {
        currentHeight += 35;
        return currentHeight;
    }

    private void addInputField(String label, String initialValue, java.util.function.Consumer<String> onTextChanged) {
        int widgetWidth = 200;
        int widgetXOffset = widgetWidth / 2;
        int tHeight = getNewHeight();

        TextFieldWidget textField = new TextFieldWidget(this.textRenderer, this.width / 2 - widgetXOffset, tHeight, widgetWidth, 20, Text.literal(label));
        textField.setMaxLength(256);
        textField.setText(initialValue);
        textField.setChangedListener(onTextChanged);
        this.addSelectableChild(textField);
        this.addDrawable(textField);

        this.addDrawable(new TextWidget(this.width / 2 - widgetXOffset, tHeight - 15, widgetWidth, 20, Text.literal(label), this.textRenderer));
    }

    private ButtonWidget addButton(String label, int width, int xOffset, ButtonWidget.PressAction action) {
        return this.addDrawableChild(ButtonWidget.builder(Text.literal(label), action)
                .dimensions(this.width / 2 - xOffset, getNewHeight() - 10, width, 20).build());
    }
}
