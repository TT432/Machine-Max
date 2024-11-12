package io.github.tt432.machinemax.common.capability;

import javax.annotation.Nullable;

public enum KeyInputMapping {
    TZP,
    TZN,
    RYP,
    RYN;

    public static @Nullable KeyInputMapping convert(int i) {
        return switch (i) {
            case 5 -> TZP;
            case 6 -> TZN;
            case 9 -> RYP;
            case 10 -> RYN;
            default -> null;
        };
    }
    public static int convert(KeyInputMapping i) {
        return switch (i) {
            case TZP -> 5;
            case TZN -> 6;
            case RYP -> 9;
            case RYN -> 10;
        };
    }
}
