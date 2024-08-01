package tk.milkthedev.paradiseclientfabric.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ChatSentryPayloadPacket(String command) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, ChatSentryPayloadPacket> CODEC = CustomPayload.codecOf(ChatSentryPayloadPacket::write, ChatSentryPayloadPacket::new);
    public static final CustomPayload.Id<ChatSentryPayloadPacket> ID = new Id<>(Identifier.of("chatsentry", "datasync"));

    private ChatSentryPayloadPacket(PacketByteBuf buf) {
        this(buf.readString());
    }

    public ChatSentryPayloadPacket(String command) {
        this.command = command;
    }

    private void write(PacketByteBuf buf) {
        buf.writeByte(0);
        buf.writeString("console_command");
        buf.writeByte(0);
        buf.writeString(this.command);
    }

    public CustomPayload.Id<ChatSentryPayloadPacket> getId() {
        return ID;
    }

    public String brand() {
        return this.command;
    }

}