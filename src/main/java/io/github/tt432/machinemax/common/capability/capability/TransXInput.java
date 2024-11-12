package io.github.tt432.machinemax.common.capability.capability;

import net.minecraft.nbt.CompoundTag;

public class TransXInput implements ITransInput {

    private double transXInput;

    @Override
    public double getInput() {
        return transXInput;
    }

    @Override
    public void setInput(double x) {
        if (x > MAX_INPUT) x = MAX_INPUT;
        else if (x < MIN_INPUT) x = MIN_INPUT;
        this.transXInput = x;
    }

    @Override
    public void addInput(double x) {
        this.transXInput += x;
        if (transXInput > MAX_INPUT) transXInput = MAX_INPUT;
        else if (transXInput < MIN_INPUT) transXInput = MIN_INPUT;
    }

    public void copyFrom(TransXInput source) {
        this.transXInput = source.getInput();
    }

    @Override
    public void saveNBTData(CompoundTag nbt) {
        nbt.putDouble("trans_x_input", transXInput);
    }

    @Override
    public void loadNBTData(CompoundTag nbt) {
        transXInput = nbt.getDouble("trans_x_input");
    }
}
