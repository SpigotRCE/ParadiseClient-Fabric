package io.github.spigotrce.chatroomold.shared.packet;

@FunctionalInterface
public interface PacketCreator {
    Packet create(PacketManager manager);
}