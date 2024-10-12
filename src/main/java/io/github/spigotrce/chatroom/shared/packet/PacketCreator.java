package io.github.spigotrce.chatroom.shared.packet;

@FunctionalInterface
public interface PacketCreator {
    Packet create(PacketManager manager);
}