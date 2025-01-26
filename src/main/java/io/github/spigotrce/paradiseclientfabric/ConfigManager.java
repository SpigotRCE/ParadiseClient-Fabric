package io.github.spigotrce.paradiseclientfabric;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final File CONFIG_FILE = new File("config/paradiseclient.json");
    private static JsonObject config;

    static {
        loadConfig();
    }

    // Charger la configuration depuis le fichier
    private static void loadConfig() {
        try {
            if (CONFIG_FILE.exists()) {
                FileReader reader = new FileReader(CONFIG_FILE);
                config = JsonParser.parseReader(reader).getAsJsonObject();
                reader.close();
            } else {
                config = new JsonObject();
                saveConfig();
            }
        } catch (IOException e) {
            config = new JsonObject();
        }
    }

    // Sauvegarder la configuration dans le fichier
    private static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            writer.write(config.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTheme() {
        return config.has("theme") ? config.get("theme").getAsString() : "ParadiseHack"; // ParadiseHack par défaut
    }

    public static void setTheme(String theme) {
        config.addProperty("theme", theme); // Mise à jour
        saveConfig(); // Sauvegarde immédiate
    }

}
