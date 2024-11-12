package io.github.tt432.machinemax.common.capability.capability;

import net.minecraft.nbt.CompoundTag;

public class TransYInput implements ITransInput {

    private double transYInput;

    @Override
    public double getInput() {
        return transYInput;
    }

    @Override
    public void setInput(double x) {
        if (x > MAX_INPUT) x = MAX_INPUT;
        else if (x < MIN_INPUT) x = MIN_INPUT;
        this.transYInput = x;
    }

    @Override
    public void addInput(double x) {
        this.transYInput += x;
        if (transYInput > MAX_INPUT) transYInput = MAX_INPUT;
        else if (transYInput < MIN_INPUT) transYInput = MIN_INPUT;
    }

    public void copyFrom(TransYInput source) {
        this.transYInput = source.getInput();
    }

    @Override
    public void saveNBTData(CompoundTag nbt) {
        nbt.putDouble("trans_y_input", transYInput);
    }

    @Override
    public void loadNBTData(CompoundTag nbt) {
        transYInput = nbt.getDouble("trans_y_input");
    }
}
