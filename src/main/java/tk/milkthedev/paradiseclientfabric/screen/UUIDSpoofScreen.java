package tk.milkthedev.paradiseclientfabric.screen;

import com.google.gson.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import tk.milkthedev.paradiseclientfabric.mod.BungeeSpoofMod;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;

import static tk.milkthedev.paradiseclientfabric.Constants.LOGGER;

public class UUIDSpoofScreen extends Screen
{
    private String status;
    private TextFieldWidget bungeeUsernameField;
    private TextFieldWidget bungeeFakeUsernameField;
    private ButtonWidget premiumButton;
    private final BungeeSpoofMod bungeeSpoofMod = ParadiseClient_Fabric.getBungeeSpoofMod();
    private final Screen parentScreen;
    private final MinecraftClient minecraftClient = MinecraftClient.getInstance();
    public UUIDSpoofScreen(Screen parentScreen)
    {
        super(Text.literal("UUID Spoof"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init()
    {
        this.status = "Stand by";

        int widgetWidth = 200;
        int widgetXOffset = widgetWidth / 2;

        this.bungeeUsernameField = new TextFieldWidget(this.textRenderer, this.width / 2 - widgetXOffset, this.height / 2 - 35, widgetWidth, 20, Text.literal("Bungee Username")); // Set width to 150 and height to 20
        this.bungeeUsernameField.setMaxLength(128);
        this.bungeeUsernameField.setText(this.bungeeSpoofMod.getBungeeUsername());
        this.addSelectableChild(this.bungeeUsernameField);
        this.addDrawable(this.bungeeUsernameField);

        this.bungeeFakeUsernameField = new TextFieldWidget(this.textRenderer, this.width / 2 - widgetXOffset, this.height / 2 - 10, widgetWidth, 20, Text.literal("Bungee FakeUsername")); // Set width to 150 and height to 20
        this.bungeeFakeUsernameField.setMaxLength(128);
        this.bungeeFakeUsernameField.setText(this.bungeeSpoofMod.getBungeeFakeUsername());
        this.addSelectableChild(this.bungeeFakeUsernameField);
        this.addDrawable(this.bungeeFakeUsernameField);

        premiumButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(bungeeSpoofMod.isBungeeUUIDPremium()? "Premium" : "Cracked"), button -> {
                    bungeeSpoofMod.setBungeeUUIDPremium(!bungeeSpoofMod.isBungeeUUIDPremium());
                    premiumButton.setMessage(Text.literal(bungeeSpoofMod.isBungeeUUIDPremium() ? "Premium" : "Cracked"));
                })
                .dimensions(this.width / 2 - widgetXOffset, this.height / 2 + 15, widgetWidth, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Spoof"), button -> spoof())
            .dimensions(this.width / 2 - widgetXOffset, this.height / 2 + 40, widgetWidth, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Exit"), button -> close())
                .dimensions(this.width / 2 - widgetXOffset, this.height / 2 + 65, widgetWidth, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.status, this.width / 2, 20, 16777215);
    }

    @Override
    public boolean shouldCloseOnEsc() {return true;}

    @Override
    public void close() {minecraftClient.setScreen(parentScreen);}

    @Override
    public void resize(MinecraftClient client, int width, int height)
    {
        String s1 = this.bungeeUsernameField.getText();
        String s2 = this.bungeeFakeUsernameField.getText();
        this.init(client, width, height);
        this.bungeeUsernameField.setText(s1);
        this.bungeeFakeUsernameField.setText(s2);
    }

    private void spoof()
    {
        this.bungeeSpoofMod.setBungeeUsername(this.bungeeUsernameField.getText());
        this.bungeeSpoofMod.setBungeeFakeUsername(this.bungeeFakeUsernameField.getText());
        if (this.bungeeSpoofMod.isBungeeUUIDPremium())
        {
            try
            {

                this.bungeeSpoofMod.setBungeeUUID(fetchUUID(this.bungeeSpoofMod.getBungeeFakeUsername()));
                this.status = "Successfully spoofed premium UUID of \"" + this.bungeeSpoofMod.getBungeeFakeUsername() + "\".";
            }
            catch (Exception e)
            {
                this.status = "Error fetching UUID, \"" + this.bungeeSpoofMod.getBungeeFakeUsername() + "\" may not be premium.";
                LOGGER.error(Arrays.toString(e.getStackTrace()));
            }
            return;
        }
        this.status = "Generating cracked UUID";
        this.bungeeSpoofMod.setBungeeUUID(UUID.nameUUIDFromBytes(("OfflinePlayer:" + bungeeSpoofMod.getBungeeFakeUsername().toLowerCase()).getBytes()).toString().replace("-", ""));
        this.status = "Successfully spoofed cracked UUID of \"" + this.bungeeSpoofMod.getBungeeFakeUsername() + "\".";
    }

    // Don't blame me, it's chatgpt's code
    public String fetchUUID(String username) throws Exception
    {
        String urlString = "https://api.mojang.com/users/profiles/minecraft/" + username;
        URL url = new URL(urlString);
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
}
