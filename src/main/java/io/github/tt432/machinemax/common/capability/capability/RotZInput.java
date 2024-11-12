package io.github.tt432.machinemax.common.capability.capability;

import net.minecraft.nbt.CompoundTag;

public class RotZInput implements IRotInput {

    private double rotZInput;

    @Override
    public double getInput() {
        return rotZInput;
    }

    @Override
    public void setInput(double x) {
        if (x > MAX_INPUT) x += 2 * MIN_INPUT;
        else if (x < MIN_INPUT) x += MAX_INPUT;
        this.rotZInput = x;
    }

    @Override
    public void addInput(double x) {
        this.rotZInput += x;
        if (rotZInput > MAX_INPUT) rotZInput += 2 * MIN_INPUT;
        else if (rotZInput < MIN_INPUT) rotZInput += MAX_INPUT;
    }

    public void copyFrom(RotZInput source) {
        this.rotZInput = source.getInput();
    }

    @Override
    public void saveNBTData(CompoundTag nbt) {
        nbt.putDouble("rot_z_input", rotZInput);
    }

    @Override
    public void loadNBTData(CompoundTag nbt) {
        rotZInput = nbt.getDouble("rot_z_input");
    }
}
