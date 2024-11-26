package io.github.tt432.machinemax.common.entity;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.entity.entity.TestCarEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

/**
 * 在此注册所有实体的属性
 * @Author 甜粽子
 */
@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class MMAttributeHandler {
    @SubscribeEvent
    public static void attr(EntityAttributeCreationEvent event) {
        event.put(MMEntities.TEST_CAR_ENTITY.get(), TestCarEntity.createLivingAttributes().build());
    }
}
