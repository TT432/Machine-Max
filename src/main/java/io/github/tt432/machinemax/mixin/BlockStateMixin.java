package io.github.tt432.machinemax.mixin;

import io.github.tt432.machinemax.common.phys.AbstractPhysThread;
import io.github.tt432.machinemax.mixin_interface.IMixinBlockState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockState.class)
public class BlockStateMixin implements IMixinBlockState {
    @Unique
    public Vec3 machine_max$roadOffset = Vec3.ZERO;

    @Override
    public Vec3 machine_Max$getRoadOffset() {
        return machine_max$roadOffset;
    }

    @Override
    public void machine_Max$setRoadOffset(Vec3 offset) {
        this.machine_max$roadOffset = offset;
    }
}
