package io.github.spigotrce.paradiseclientfabric.chatroom.common.model;

public record DiscordModel(String token, long serverID, boolean autoVerify, long verificationChannelID, String webhookAccountLogging) {
}
