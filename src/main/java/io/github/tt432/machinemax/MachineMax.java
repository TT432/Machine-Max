package io.github.tt432.machinemax;

import io.github.tt432.machinemax.common.entity.MMMEntities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(MachineMax.MOD_ID)
public class MachineMax {
    public static final String MOD_ID = "machine_max";
    public MachineMax(IEventBus bus){
        MMMEntities.ENTITIES.register(bus);
    }
}
