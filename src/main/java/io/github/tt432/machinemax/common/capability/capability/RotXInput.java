package io.github.tt432.machinemax.common.capability.capability;

import net.minecraft.nbt.CompoundTag;

public class RotXInput implements IRotInput {

    private double rotXInput;

    @Override
    public double getInput() {
        return rotXInput;
    }

    @Override
    public void setInput(double x) {
        if (x > MAX_INPUT) x += 2 * MIN_INPUT;
        else if (x < MIN_INPUT) x += MAX_INPUT;
        this.rotXInput = x;
    }

    @Override
    public void addInput(double x) {
        this.rotXInput += x;
        if (rotXInput > MAX_INPUT) rotXInput += 2 * MIN_INPUT;
        else if (rotXInput < MIN_INPUT) rotXInput += MAX_INPUT;
    }

    public void copyFrom(RotXInput source) {
        this.rotXInput = source.getInput();
    }

    @Override
    public void saveNBTData(CompoundTag nbt) {
        nbt.putDouble("rot_x_input", rotXInput);
    }

    @Override
    public void loadNBTData(CompoundTag nbt) {
        rotXInput = nbt.getDouble("rot_x_input");
    }
}
