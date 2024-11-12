package io.github.tt432.machinemax.common.capability.capability;

import net.minecraft.nbt.CompoundTag;

public interface IInput {
    double getInput();
    void setInput(double x);
    void addInput(double x);
    void saveNBTData(CompoundTag nbt);
    void loadNBTData(CompoundTag nbt);
}
