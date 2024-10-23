package io.github.tt432.machinemax.common.entity.part.slot;

/**
 * 占位符槽位类，不可安装任何模块
 */
public class UndefinedModuleSlot extends BasicModuleSlot{
    @Override
    public boolean slotConditionCheck() {
        return false;
    }
}
