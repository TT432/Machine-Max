package io.github.tt432.machinemax.client.renderer;

import io.github.tt432.machinemax.common.entity.MMMEntities;
import io.github.tt432.machinemax.common.entity.TestCarEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * 在此注册所有Renderer
 * @Author 甜粽子
 */
@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class MMMRenderHandler {
    @SubscribeEvent//注册每个实体渲染器
    public static void onEntityRendererRegistry(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(MMMEntities.TEST_CAR_ENTITY.get(), TestCarEntityRenderer::new);
    }
}
