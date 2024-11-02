package io.github.tt432.machinemax.client.renderer;

import io.github.tt432.machinemax.common.entity.MMEntities;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;
import io.github.tt432.machinemax.common.entity.part.TestCarChassisPart;
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
            if (event.getRenderer(MMEntities.TEST_CAR_ENTITY.get()) instanceof MMEntityRenderer renderer) {
                //TODO:想办法拿到渲染中的实体
                //BasicEntity entity = ;//获取要渲染的实体
                //TODO:想办法拿到实例化的Part对象
                //renderer.addLayer(new MMPartRenderLayer(renderer));//首先渲染根部件模型
//                for(AbstractMMPart part : entity.CORE_PART){
//                    renderer.addLayer(new MMPartRenderLayer(renderer, part));//再渲染所有与之连接的部件的模型
//                }
            }
        });
    }
    @SubscribeEvent//注册Layer附着点（？
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(MMEmptyModel.LAYER_LOCATION, MMEmptyModel::createBodyLayer);
    }
}
