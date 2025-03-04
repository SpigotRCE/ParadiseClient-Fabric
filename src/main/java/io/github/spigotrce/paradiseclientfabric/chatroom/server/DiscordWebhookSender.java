package io.github.spigotrce.paradiseclientfabric.chatroom.server;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class to send a message to discord webhook
 */
public class DiscordWebhookSender {
    /**
     * webhookUrl string representing the discord webhook url
     */
    private final String webhookUrl;

    /**
     * Constructor to initialize webhookUrl and logger
     *
     * @param webhookUrl webhook url string
     */
    public DiscordWebhookSender(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    /**
     * Method to send a message to discord webhook using the provided json payload
     *
     * @param jsonPayload json payload string
     */
    public void sendMessage(String jsonPayload) throws IOException {
        HttpURLConnection connection = getHttpURLConnection();


        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();

        if (responseCode < 200 || responseCode > 300)
            throw new IOException("Failed to send message: HTTP error code: " + responseCode);

        connection.disconnect();
    }

    /**
     * Method to create a HttpURLConnection object with necessary headers
     *
     * @return HttpURLConnection object
     * @throws IOException if an error occurs while creating the connection
     */
    private @NotNull HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL(webhookUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        return connection;
    }
}