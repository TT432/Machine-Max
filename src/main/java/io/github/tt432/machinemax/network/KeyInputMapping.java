package io.github.tt432.machinemax.network;

import javax.annotation.Nullable;

public enum KeyInputMapping {
    TX,
    TY,
    TZ,
    RX,
    RY,
    RZ;

    public static @Nullable KeyInputMapping convert(int i) {
        return switch (i) {
            case 1 -> TX;
            case 2 -> TY;
            case 3 -> TZ;
            case 4 -> RX;
            case 5 -> RY;
            case 6 -> RZ;
            default -> null;
        };
    }
    public static int convert(KeyInputMapping i) {
        return switch (i) {
            case TX -> 1;
            case TY -> 2;
            case TZ -> 3;
            case RX -> 4;
            case RY -> 5;
            case RZ -> 6;
        };
    }
}
