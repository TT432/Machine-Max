package io.github.tt432.machinemax.mixin_interfaces;

import io.github.tt432.machinemax.common.phys.AbstractPhysThread;

public interface IMixinLevel {
    AbstractPhysThread machine_Max$getPhysThread();
    void machine_Max$setPhysThread(AbstractPhysThread thread);
}
