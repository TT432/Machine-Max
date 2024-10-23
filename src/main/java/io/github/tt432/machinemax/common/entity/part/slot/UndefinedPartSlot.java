package io.github.tt432.machinemax.common.entity.part.slot;

import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;

/**
 * 占位符槽位类，不可安装任何部件
 */
public class UndefinedPartSlot extends BasicPartSlot{
    @Override
    public boolean slotConditionCheck(AbstractMMPart part) {
        return false;
    }
}
