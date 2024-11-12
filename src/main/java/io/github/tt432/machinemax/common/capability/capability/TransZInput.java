package io.github.tt432.machinemax.common.capability.capability;

import net.minecraft.nbt.CompoundTag;

public class TransZInput implements ITransInput {

    private double transZInput;

    @Override
    public double getInput() {
        return transZInput;
    }

    @Override
    public void setInput(double x) {
        if (x > MAX_INPUT) x = MAX_INPUT;
        else if (x < MIN_INPUT) x = MIN_INPUT;
        this.transZInput = x;
    }

    @Override
    public void addInput(double x) {
        this.transZInput += x;
        if (transZInput > MAX_INPUT) transZInput = MAX_INPUT;
        else if (transZInput < MIN_INPUT) transZInput = MIN_INPUT;
    }

    public void copyFrom(TransZInput source) {
        this.transZInput = source.getInput();
    }

    @Override
    public void saveNBTData(CompoundTag nbt) {
        nbt.putDouble("trans_z_input", transZInput);
    }

    @Override
    public void loadNBTData(CompoundTag nbt) {
        transZInput = nbt.getDouble("trans_z_input");
    }
}
