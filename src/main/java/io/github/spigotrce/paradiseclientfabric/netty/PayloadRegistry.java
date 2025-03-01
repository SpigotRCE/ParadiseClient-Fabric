package io.github.spigotrce.paradiseclientfabric.netty;

import io.github.spigotrce.paradiseclientfabric.protocol.ProtocolConstants;

// TODO: Migrate to Bungee protocol
enum PayloadRegistry {
    MINECRAFT_1_8(ProtocolConstants.MINECRAFT_1_8, 63),
    MINECRAFT_1_8_1(ProtocolConstants.MINECRAFT_1_8_1, 63),
    MINECRAFT_1_8_2(ProtocolConstants.MINECRAFT_1_8_2, 63),
    MINECRAFT_1_8_3(ProtocolConstants.MINECRAFT_1_8_3, 63),
    MINECRAFT_1_8_4(ProtocolConstants.MINECRAFT_1_8_4, 63),
    MINECRAFT_1_8_5(ProtocolConstants.MINECRAFT_1_8_5, 63),
    MINECRAFT_1_8_6(ProtocolConstants.MINECRAFT_1_8_6, 63),
    MINECRAFT_1_8_7(ProtocolConstants.MINECRAFT_1_8_7, 63),
    MINECRAFT_1_8_8(ProtocolConstants.MINECRAFT_1_8_8, 63),
    MINECRAFT_1_8_9(ProtocolConstants.MINECRAFT_1_8_9, 63),

    MINECRAFT_1_9(ProtocolConstants.MINECRAFT_1_9, 24),
    MINECRAFT_1_9_1(ProtocolConstants.MINECRAFT_1_9_1, 24),
    MINECRAFT_1_9_2(ProtocolConstants.MINECRAFT_1_9_2, 24),
    MINECRAFT_1_9_3(ProtocolConstants.MINECRAFT_1_9_3, 24),
    MINECRAFT_1_9_4(ProtocolConstants.MINECRAFT_1_9_4, 24),

    MINECRAFT_1_10(ProtocolConstants.MINECRAFT_1_10, 24),
    MINECRAFT_1_10_1(ProtocolConstants.MINECRAFT_1_10_1, 24),
    MINECRAFT_1_10_2(ProtocolConstants.MINECRAFT_1_10_2, 24),

    MINECRAFT_1_11(ProtocolConstants.MINECRAFT_1_11, 24),
    MINECRAFT_1_11_1(ProtocolConstants.MINECRAFT_1_11_1, 24),
    MINECRAFT_1_11_2(ProtocolConstants.MINECRAFT_1_11_2, 24),

    MINECRAFT_1_12(ProtocolConstants.MINECRAFT_1_12, 24),
    MINECRAFT_1_12_1(ProtocolConstants.MINECRAFT_1_12_1, 24),
    MINECRAFT_1_12_2(ProtocolConstants.MINECRAFT_1_12_2, 24),

    MINECRAFT_1_13(ProtocolConstants.MINECRAFT_1_13, 25),
    MINECRAFT_1_13_1(ProtocolConstants.MINECRAFT_1_13_1, 25),
    MINECRAFT_1_13_2(ProtocolConstants.MINECRAFT_1_13_2, 25),

    MINECRAFT_1_14(ProtocolConstants.MINECRAFT_1_14, 24),
    MINECRAFT_1_14_1(ProtocolConstants.MINECRAFT_1_14_1, 24),
    MINECRAFT_1_14_2(ProtocolConstants.MINECRAFT_1_14_2, 24),
    MINECRAFT_1_14_3(ProtocolConstants.MINECRAFT_1_14_3, 24),
    MINECRAFT_1_14_4(ProtocolConstants.MINECRAFT_1_14_4, 24),

    MINECRAFT_1_15(ProtocolConstants.MINECRAFT_1_15, 25),
    MINECRAFT_1_15_1(ProtocolConstants.MINECRAFT_1_15_1, 25),
    MINECRAFT_1_15_2(ProtocolConstants.MINECRAFT_1_15_2, 25),

    MINECRAFT_1_16(ProtocolConstants.MINECRAFT_1_16, 24),
    MINECRAFT_1_16_1(ProtocolConstants.MINECRAFT_1_16_1, 24),
    MINECRAFT_1_16_2(ProtocolConstants.MINECRAFT_1_16_2, 23),
    MINECRAFT_1_16_3(ProtocolConstants.MINECRAFT_1_16_3, 23),
    MINECRAFT_1_16_4(ProtocolConstants.MINECRAFT_1_16_4, 23),
    MINECRAFT_1_16_5(ProtocolConstants.MINECRAFT_1_16_5, 23),

    MINECRAFT_1_17(ProtocolConstants.MINECRAFT_1_17, 24),
    MINECRAFT_1_17_1(ProtocolConstants.MINECRAFT_1_17_1, 24),

    MINECRAFT_1_18(ProtocolConstants.MINECRAFT_1_18, 24),
    MINECRAFT_1_18_1(ProtocolConstants.MINECRAFT_1_18_1, 24),
    MINECRAFT_1_18_2(ProtocolConstants.MINECRAFT_1_18_2, 24),

    MINECRAFT_1_19(ProtocolConstants.MINECRAFT_1_19, 21),
    MINECRAFT_1_19_1(ProtocolConstants.MINECRAFT_1_19_1, 22),
    MINECRAFT_1_19_2(ProtocolConstants.MINECRAFT_1_19_2, 22),
    MINECRAFT_1_19_3(ProtocolConstants.MINECRAFT_1_19_3, 21),
    MINECRAFT_1_19_4(ProtocolConstants.MINECRAFT_1_19_4, 23),

    MINECRAFT_1_20(ProtocolConstants.MINECRAFT_1_20, 24),
    MINECRAFT_1_20_1(ProtocolConstants.MINECRAFT_1_20_1, 24),
    MINECRAFT_1_20_2(ProtocolConstants.MINECRAFT_1_20_2, 24),
    MINECRAFT_1_20_3(ProtocolConstants.MINECRAFT_1_20_3, 24),
    MINECRAFT_1_20_4(ProtocolConstants.MINECRAFT_1_20_4, 24),
    MINECRAFT_1_20_5(ProtocolConstants.MINECRAFT_1_20_5, 25),

    MINECRAFT_1_21(ProtocolConstants.MINECRAFT_1_21, 25),
    MINECRAFT_1_21_1(ProtocolConstants.MINECRAFT_1_21_1, 25),
    MINECRAFT_1_21_2(ProtocolConstants.MINECRAFT_1_21_2, 25),
    MINECRAFT_1_21_3(ProtocolConstants.MINECRAFT_1_21_3, 25),
    MINECRAFT_1_21_4(ProtocolConstants.MINECRAFT_1_21_4, 25);

    final int protocolVersion;
    final int packetId;

    PayloadRegistry(int protocolVersion, int packetId) {
        this.protocolVersion = protocolVersion;
        this.packetId = packetId;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public int getPacketId() {
        return packetId;
    }

    public static boolean isValidPacket(int protocolVersion, int packetId) {
        for (PayloadRegistry registry : values())
            if (registry.getProtocolVersion() == protocolVersion && registry.getPacketId() == packetId)
                return true;
        return false;
    }
}
