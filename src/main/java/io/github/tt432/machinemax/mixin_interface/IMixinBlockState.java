package io.github.tt432.machinemax.mixin_interface;

import net.minecraft.world.phys.Vec3;

public interface IMixinBlockState {
    Vec3 machine_Max$getRoadOffset();
    void machine_Max$setRoadOffset(Vec3 roadOffset);
}
