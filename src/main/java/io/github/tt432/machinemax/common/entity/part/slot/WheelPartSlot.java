package io.github.tt432.machinemax.common.entity.part.slot;

import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;

public class WheelPartSlot extends BasicPartSlot{
    @Override
    public boolean slotConditionCheck(AbstractMMPart part) {
        return part.PART_TYPE == AbstractMMPart.partTypes.WHEEL;
    }
}
