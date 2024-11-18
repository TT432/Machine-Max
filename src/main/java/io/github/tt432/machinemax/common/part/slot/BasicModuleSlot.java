package io.github.tt432.machinemax.common.part.slot;

public class BasicModuleSlot {

    boolean slotConditionCheck(){//TODO:这里需要一个模块类作为入参
        return true;//如有为此槽位指定安装条件的需要，继承此类并重载此方法
    }
}
