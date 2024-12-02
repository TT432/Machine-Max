package io.github.tt432.machinemax.mixin;

import io.github.tt432.machinemax.common.phys.AbstractPhysThread;
import io.github.tt432.machinemax.mixin_interface.IMixinLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Level.class)
public class LevelMixin implements IMixinLevel {
    @Unique
    public AbstractPhysThread machine_max$physThread;

    @Override
    public AbstractPhysThread machine_Max$getPhysThread() {
        return machine_max$physThread;
    }

    @Override
    public void machine_Max$setPhysThread(AbstractPhysThread thread) {
        this.machine_max$physThread = thread;
    }
}
