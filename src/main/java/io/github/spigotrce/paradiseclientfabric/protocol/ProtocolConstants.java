package io.github.spigotrce.paradiseclientfabric.protocol;

import com.google.common.collect.ImmutableList;
import java.util.List;

public class ProtocolConstants {
    public static final int MINECRAFT_1_8 = 47;
    public static final int MINECRAFT_1_8_1 = 47;
    public static final int MINECRAFT_1_8_2 = 47;
    public static final int MINECRAFT_1_8_3 = 47;
    public static final int MINECRAFT_1_8_4 = 47;
    public static final int MINECRAFT_1_8_5 = 47;
    public static final int MINECRAFT_1_8_6 = 47;
    public static final int MINECRAFT_1_8_7 = 47;
    public static final int MINECRAFT_1_8_8 = 47;
    public static final int MINECRAFT_1_8_9 = 47;
    public static final int MINECRAFT_1_9 = 107;
    public static final int MINECRAFT_1_9_1 = 108;
    public static final int MINECRAFT_1_9_2 = 109;
    public static final int MINECRAFT_1_9_3 = 109;
    public static final int MINECRAFT_1_9_4 = 110;
    public static final int MINECRAFT_1_10 = 210;
    public static final int MINECRAFT_1_10_1 = 210;
    public static final int MINECRAFT_1_10_2 = 210;
    public static final int MINECRAFT_1_11 = 315;
    public static final int MINECRAFT_1_11_1 = 316;
    public static final int MINECRAFT_1_11_2 = 316;
    public static final int MINECRAFT_1_12 = 335;
    public static final int MINECRAFT_1_12_1 = 338;
    public static final int MINECRAFT_1_12_2 = 340;
    public static final int MINECRAFT_1_13 = 393;
    public static final int MINECRAFT_1_13_1 = 401;
    public static final int MINECRAFT_1_13_2 = 404;
    public static final int MINECRAFT_1_14 = 477;
    public static final int MINECRAFT_1_14_1 = 480;
    public static final int MINECRAFT_1_14_2 = 485;
    public static final int MINECRAFT_1_14_3 = 490;
    public static final int MINECRAFT_1_14_4 = 498;
    public static final int MINECRAFT_1_15 = 573;
    public static final int MINECRAFT_1_15_1 = 575;
    public static final int MINECRAFT_1_15_2 = 578;
    public static final int MINECRAFT_1_16 = 735;
    public static final int MINECRAFT_1_16_1 = 736;
    public static final int MINECRAFT_1_16_2 = 751;
    public static final int MINECRAFT_1_16_3 = 753;
    public static final int MINECRAFT_1_16_4 = 754;
    public static final int MINECRAFT_1_16_5 = 754;
    public static final int MINECRAFT_1_17 = 755;
    public static final int MINECRAFT_1_17_1 = 756;
    public static final int MINECRAFT_1_18 = 757;
    public static final int MINECRAFT_1_18_1 = 757;
    public static final int MINECRAFT_1_18_2 = 758;
    public static final int MINECRAFT_1_19 = 759;
    public static final int MINECRAFT_1_19_1 = 760;
    public static final int MINECRAFT_1_19_2 = 760;
    public static final int MINECRAFT_1_19_3 = 761;
    public static final int MINECRAFT_1_19_4 = 762;
    public static final int MINECRAFT_1_20 = 763;
    public static final int MINECRAFT_1_20_1 = 763;
    public static final int MINECRAFT_1_20_2 = 764;
    public static final int MINECRAFT_1_20_3 = 765;
    public static final int MINECRAFT_1_20_4 = 765;
    public static final int MINECRAFT_1_20_5 = 766;
    public static final int MINECRAFT_1_21 = 767;
    public static final int MINECRAFT_1_21_1 = 767;
    public static final int MINECRAFT_1_21_2 = 768;
    public static final int MINECRAFT_1_21_3 = 768;
    public static final int MINECRAFT_1_21_4 = 769;
    public static final List<Integer> SUPPORTED_VERSION_IDS;

    static {
        ImmutableList.Builder<Integer> supportedVersionIds = ImmutableList.builder();
        for (int i = 1_8; i <= 1_21_4; i++)
            try {
                supportedVersionIds.add((Integer) ProtocolConstants.class.getField("MINECRAFT_" + i).get(null));
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        SUPPORTED_VERSION_IDS = supportedVersionIds.build();
    }
}
