package io.github.spigotrce.paradiseclientfabric.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityAssets;

import io.github.spigotrce.paradiseclientfabric.Constants;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Objects;

/**
 * Implements Runnable interface to run Discord Rich Presence functionality.
 * This class sets up the Discord RPC client, updates the presence status, and handles the game's state.
 *
 * @author SpigotRCE
 * @since 2.17
 */
@SuppressWarnings("BusyWait")
public class RPC implements Runnable {

    @Override
    public void run() {
        this.loadDiscordRPC();

        try {
            File discordDll = new File(MinecraftClient.getInstance().runDirectory, "paradise/discord/discord_game_sdk.dll");
            Core.init(discordDll);
        } catch (Exception e) {
            Constants.LOGGER.error("Failed to initialize Discord SDK: {}", e.getMessage());
            return;
        }

        try (CreateParams params = new CreateParams()) {
            params.setClientID(1164104022265974784L);
            params.setFlags(CreateParams.getDefaultFlags());

            try (Core core = new Core(params)) {
                try (Activity activity = new Activity()) {
                    ActivityAssets assets = activity.assets();
                    assets.setLargeImage("paradise_logo");
                    assets.setLargeText("Paradise Client");
                    
                    activity.setDetails("In Menu");
                    activity.timestamps().setStart(Instant.now());
                    core.activityManager().updateActivity(activity);

                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            core.runCallbacks();

                            if (ParadiseClient_Fabric.networkMod.isConnected) {
                                activity.setDetails("Playing on a server");
                                activity.setState(
                                    Objects.isNull(MinecraftClient.getInstance().getCurrentServerEntry()) 
                                        ? "Hidden" 
                                        : MinecraftClient.getInstance().getCurrentServerEntry().address
                                );
                            } else {
                                activity.setDetails("In Menu");
                                activity.setState("");
                            }

                            core.activityManager().updateActivity(activity);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Constants.LOGGER.error("Interrupted Discord RPC thread: {}", e.getMessage());
                            Thread.currentThread().interrupt();
                            break;
                        } catch (Exception e) {
                            Constants.LOGGER.error("Error while updating Discord activity: {}", e.getMessage());
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
        String gameDir = MinecraftClient.getInstance().runDirectory.getAbsolutePath();
        String filePath = gameDir + File.separator + "paradise" + File.separator + "discord" + File.separator;
        File dir = new File(filePath);

        try {
            dir.mkdirs();
            File file = new File(filePath + "discord_game_sdk.dll");

            if (!file.exists()) {
                Constants.LOGGER.info("Copying missing Discord game SDK file: {}", file.getPath());
                try (InputStream inputStream = getClass().getResourceAsStream("/assets/paradise/discord/discord_game_sdk.dll")) {
                    if (inputStream == null) {
                        throw new IllegalStateException("Discord SDK file not found in resources!");
                    }
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (Exception e) {
            Constants.LOGGER.error("Error copying Discord game SDK file: {}", e.getMessage());
        }
    }
}
