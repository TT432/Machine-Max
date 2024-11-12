package io.github.tt432.machinemax.common.capability.capability;

import net.minecraft.nbt.CompoundTag;

public class RotYInput implements IRotInput {

    private double rotYInput;

    @Override
    public double getInput() {
        return rotYInput;
    }

    @Override
    public void setInput(double x) {
        if (x > MAX_INPUT) x += 2 * MIN_INPUT;
        else if (x < MIN_INPUT) x += MAX_INPUT;
        this.rotYInput = x;
    }

    @Override
    public void addInput(double x) {
        this.rotYInput += x;
        if (rotYInput > MAX_INPUT) rotYInput += 2 * MIN_INPUT;
        else if (rotYInput < MIN_INPUT) rotYInput += MAX_INPUT;
    }

    public void copyFrom(RotYInput source) {
        this.rotYInput = source.getInput();
    }

    @Override
    public void saveNBTData(CompoundTag nbt) {
        nbt.putDouble("rot_y_input", rotYInput);
    }

    @Override
    public void loadNBTData(CompoundTag nbt) {
        rotYInput = nbt.getDouble("rot_y_input");
    }
}
