package io.github.spigotrce.paradiseclientfabric.discord;

//import de.jcm.discordgamesdk.Core;
//import de.jcm.discordgamesdk.CreateParams;
//import de.jcm.discordgamesdk.activity.Activity;

import io.github.spigotrce.paradiseclientfabric.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
//
//        try {
//            Core.init(new File(MinecraftClient.getInstance().runDirectory.getAbsolutePath() + File.separator + "paradise" + File.separator + "discord" + File.separator + "discord_game_sdk.dll"));
//        } catch (Exception e) {
//            Constants.LOGGER.error("Failed to initialize Discord SDK: {}", e.getMessage());
//            return;
//        }
//
//        try (CreateParams params = new CreateParams()) {
//            params.setClientID(1164104022265974784L);
//            params.setFlags(CreateParams.getDefaultFlags());
//
//            try (Core core = new Core(params)) {
//                try (Activity activity = new Activity()) {
//                    activity.setDetails("In Menu");
//                    activity.timestamps().setStart(Instant.now());
//
//                    core.activityManager().updateActivity(activity);
//
//                    while (true) {
//                        try {
//                            core.runCallbacks();
//
//                            if (ParadiseClient_Fabric.getNetworkMod().isConnected) {
//                                activity.setDetails("Playing on a server");
//                                activity.setState(Objects.isNull(MinecraftClient.getInstance().getCurrentServerEntry()) ? "Hidden" : MinecraftClient.getInstance().getCurrentServerEntry().address);
//                            } else {
//                                activity.setDetails("In Menu");
//                                activity.setState("");
//                            }
//
//                            core.activityManager().updateActivity(activity);
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            Constants.LOGGER.error("Interrupted Discord RPC thread: {}", e.getMessage());
//                            break;
//                        } catch (Exception e) {
//                            Constants.LOGGER.error("Error while updating Discord activity: {}", e.getMessage());
//                        }
//                    }
//                }
//            }
//        }
    }

    /**
     * Loads the Discord Game SDK DLL file from the resource manager and copies it to the game directory.
     */
    private void loadDiscordRPC() {
        Identifier identifier = Identifier.of(Constants.MOD_ID, "discord/discord_game_sdk.dll");

        String gameDir = MinecraftClient.getInstance().runDirectory.getAbsolutePath();
        String filePath = gameDir + File.separator + "paradise" + File.separator;
        File dir = new File(filePath);

        try {
            dir.mkdirs();
            File file = new File(filePath + identifier.getPath());

            if (!file.exists()) {
                Constants.LOGGER.info("Copying missing Discord game SDK file: {}", file.getPath());
                try (InputStream inputStream = MinecraftClient.getInstance().getResourceManager().getResource(identifier).get().getInputStream()) {
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    Constants.LOGGER.error("Error copying Discord game SDK file: {}", file.getPath(), e);
                }
            }
        } catch (Exception e) {
            Constants.LOGGER.error("Error creating directory: {}", filePath, e);
        }
    }
}
