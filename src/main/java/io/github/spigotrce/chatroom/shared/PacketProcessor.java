package io.github.spigotrce.chatroom.shared;

/**
 * <p>The base of the chatroom client & server.</p>
 */
public class PacketProcessor<K extends Enum<?>> {

    public DoubleSidedEnumRegistry<PacketType, K> registry;

    /**
     * <p>Creates a new packet processor.</p>
     * @param handlers the handlers.
     */
    public PacketProcessor(K[] handlers) {
        this.registry = new DoubleSidedEnumRegistry<>(PacketType.values(), handlers);
    }

}
