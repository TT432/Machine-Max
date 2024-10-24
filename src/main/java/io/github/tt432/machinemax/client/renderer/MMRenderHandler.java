package io.github.tt432.machinemax.client.renderer;

import io.github.tt432.machinemax.common.entity.MMEntities;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
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
        event.registerEntityRenderer(MMEntities.BASIC_ENTITY.get(), MMEntityRenderer::new);
        event.registerEntityRenderer(MMEntities.TEST_CAR_ENTITY.get(), TestCarEntityRenderer::new);
    }
    @SubscribeEvent//注册模型部件渲染器
    public static void onLayersAdding(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(model -> {
            if (event.getSkin(model) instanceof MMEntityRenderer renderer) {
                //renderer.addLayer(new MMPartRenderLayer(renderer));
            }
        });
    }
}
