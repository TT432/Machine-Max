package io.github.tt432.machinemax.client.renderer;

import io.github.tt432.machinemax.common.entity.MMEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * 在此注册所有Renderer和Layer
 * @Author 甜粽子
 */
@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class MMRenderHandler {
    @SubscribeEvent//注册每个实体渲染器
    public static void onEntityRendererRegistry(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(MMEntities.TEST_CAR_ENTITY.get(), MMEntityRenderer::new);
        event.registerEntityRenderer(MMEntities.AE86_ENTITY.get(), MMEntityRenderer::new);
    }
}
