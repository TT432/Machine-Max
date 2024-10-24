package io.github.tt432.machinemax.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.tt432.eyelib.capability.RenderData;
import io.github.tt432.eyelib.capability.component.AnimationComponent;
import io.github.tt432.eyelib.client.ClientTickHandler;
import io.github.tt432.eyelib.client.animation.BrAnimator;
import io.github.tt432.eyelib.client.loader.BrModelLoader;
import io.github.tt432.eyelib.client.render.BrModelTextures;
import io.github.tt432.eyelib.client.render.ModelRenderer;
import io.github.tt432.eyelib.client.render.RenderParams;
import io.github.tt432.eyelib.client.render.visitor.BuiltInBrModelRenderVisitors;
import io.github.tt432.eyelib.client.render.visitor.ModelRenderVisitorList;
import io.github.tt432.eyelib.util.math.EyeMath;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.List;

public class MMPartRenderLayer extends RenderLayer<BasicEntity, EntityModel<BasicEntity>>{

    public MMPartRenderLayer(RenderLayerParent<BasicEntity,EntityModel<BasicEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, BasicEntity pLivingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        pPoseStack.pushPose();
//        getParentModel().body.translateAndRotate(pPoseStack);
//
//        AnimationComponent animationComponent = new AnimationComponent();
//        animationComponent.setup(FOX_TAIL_MODEL, FOX_TAIL_MODEL);
//        var infos = BrAnimator.tickAnimation(animationComponent,
//                RenderData.getComponent(pLivingEntity).getScope(), ClientTickHandler.getTick() + pPartialTick);
//        RenderType renderType = RenderType.entitySolid(FOX_TAIL_TEXTURE);
//        ModelRenderer.render(new RenderParams(
//                        pLivingEntity,
//                        pPoseStack.last().copy(),
//                        pPoseStack,
//                        renderType,
//                        pBuffer.getBuffer(renderType),
//                        pPackedLight,
//                        LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0)
//                ), BrModelLoader.getModel(FOX_TAIL_MODEL), infos,
//                new BrModelTextures.TwoSideInfoMap(new HashMap<>()),
//                new ModelRenderVisitorList(List.of(BuiltInBrModelRenderVisitors.BLANK.get())));
        pPoseStack.popPose();
    }
}
