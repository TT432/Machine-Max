package io.github.tt432.machinemax.common.capability;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.capability.capability.RotYInput;
import io.github.tt432.machinemax.common.capability.capability.TransZInput;
import io.github.tt432.machinemax.common.capability.provider.RotYProvider;
import io.github.tt432.machinemax.common.capability.provider.TransZProvider;
import io.github.tt432.machinemax.common.entity.MMEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/**
 * 在此注册所有能力
 *
 * @author 甜粽子
 */
public class MMCapabilities {
    public static final EntityCapability<TransZInput, Void> TRANS_Z_CAPABILITY =
            EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "trans_z_capability"),
                    TransZInput.class);
    public static final EntityCapability<RotYInput, Void> ROT_Y_CAPABILITY =
            EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "trans_z_capability"),
                    RotYInput.class);

    @EventBusSubscriber(modid = MachineMax.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class EventBus {
        @SubscribeEvent
        private static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.registerEntity(
                    TRANS_Z_CAPABILITY,
                    EntityType.PLAYER,
                    new TransZProvider());
            event.registerEntity(
                    ROT_Y_CAPABILITY,
                    EntityType.PLAYER,
                    new RotYProvider());
        }
    }
}
