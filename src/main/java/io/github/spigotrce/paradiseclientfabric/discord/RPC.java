package io.github.spigotrce.paradiseclientfabric.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Objects;

/**
 * Implements Runnable interface to run Discord Rich Presence functionality.
 * This class sets up the Discord RPC client, updates the presence status, and handles the game's state.
 *
 * @author SpigotRCE
 * @since 2.17
 */
public class RPC implements Runnable {
    @Override
    public void run() {
        this.loadDiscordRPC();

        // Initialize Discord Game SDK with the DLL file
        Core.init(new File(MinecraftClient.getInstance().runDirectory.getAbsolutePath() + "\\paradise\\discord\\discord_game_sdk.dll"));

        try (CreateParams params = new CreateParams()) {
            // Set the client ID for the Discord application
            params.setClientID(1164104022265974784L);
            params.setFlags(CreateParams.getDefaultFlags());

            try (Core core = new Core(params)) {
                try (Activity activity = new Activity()) {
                    // Set initial activity details and timestamp
                    activity.setDetails("In Menu");
                    activity.timestamps().setStart(Instant.now());
                    // Uncomment to set large image
//                    activity.assets().setLargeImage(DiscordPresenceConstants.IMAGE);
                    core.activityManager().updateActivity(activity);

                    // Run callbacks forever
                    while (true) {
                        core.runCallbacks();
                        try {
                            // Update activity details and state based on game state
                            if (ParadiseClient_Fabric.getNetworkMod().isConnected) {
                                activity.setDetails("Playing on a server");
                                activity.setState(((!Objects.isNull(MinecraftClient.getInstance().getCurrentServerEntry()) && ParadiseClient_Fabric.getHudMod().showServerIP) ? MinecraftClient.getInstance().getCurrentServerEntry().address : "Hidden"));
                            } else {
                                // Uncomment to set activity details based on current screen
//                                activity.setDetails(Objects.isNull(ParadiseClient_Fabric.getMiscMod().currentScreen) ? "In Menu" : Objects.isNull(ParadiseClient_Fabric.getMiscMod().currentScreen.getTitle()) ? "In Menu" : ParadiseClient_Fabric.getMiscMod().currentScreen.getTitle().getLiteralString());
                                activity.setDetails("In Menu");
                                activity.setState("");
                            }
                            core.activityManager().updateActivity(activity);
                            Thread.sleep(16);
                        } catch (InterruptedException e) {
                            Constants.LOGGER.error("Interrupted Discord RPC thread: {}", e.getMessage());
                        }
                    }

                }
            }
        }
    }

    /**
     * Loads the Discord Game SDK DLL file from the resource manager and copies it to the game directory.
     */
    private void loadDiscordRPC() {
        Identifier identifier = Identifier.of(Constants.MOD_ID, "discord\discord_game_sdk.dll");

        String gameDir = MinecraftClient.getInstance().runDirectory.getAbsolutePath();
        String filePath = gameDir + "\\paradise\\";
        File dir = new File(filePath);

        try {
            dir.mkdirs();

            File file = new File(filePath + identifier.getPath());

            file.getParentFile().mkdirs();

            if (!file.exists()) {
                Constants.LOGGER.info("Copying missing Discord game SDK file: {}", file.getPath());
                file.createNewFile();
            }

            try (InputStream inputStream = MinecraftClient.getInstance().getResourceManager().getResource(identifier).get().getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(file)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                Constants.LOGGER.error("Error Discord game SDK file: {}", file.getPath());
            }
        } catch (IOException e) {
            Constants.LOGGER.error("Error creating directory: {}", filePath);
        }
    }
}
