package io.github.tt432.machinemax.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;
import io.github.tt432.machinemax.common.entity.part.TestCarChassisPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.List;

import static io.github.tt432.machinemax.client.renderer.TestCarEntityRenderer.*;
import static io.github.tt432.machinemax.common.entity.part.AbstractMMPart.*;

public class MMPartRenderLayer extends RenderLayer<BasicEntity, MMEmptyModel<BasicEntity>>{

    public MMPartRenderLayer(RenderLayerParent<BasicEntity,MMEmptyModel<BasicEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, BasicEntity pLivingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        pPoseStack.pushPose();
        //TODO:检查模型本体和Layer的旋转
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180));//很奇怪，Layer添加的模型翻了个面，转回来
        pPoseStack.mulPose(Axis.YN.rotationDegrees(90));//很奇怪，Layer添加的模型翻了个面，转回来
        getParentModel().root.translateAndRotate(pPoseStack);
        AnimationComponent animationComponent = new AnimationComponent();
        animationComponent.setup(TestCarChassisPart.PART_ANI_CONTROLLER, TestCarChassisPart.PART_ANIMATION);//加载动画控制器和动画包
        var infos = BrAnimator.tickAnimation(animationComponent,//播放动画
                RenderData.getComponent(pLivingEntity).getScope(), ClientTickHandler.getTick() + partialTick);
        RenderType renderType = RenderType.entitySolid(TestCarChassisPart.PART_TEXTURE);
        ModelRenderer.render(new RenderParams(
                        pLivingEntity,
                        pPoseStack.last().copy(),
                        pPoseStack,
                        renderType,
                        pBuffer.getBuffer(renderType),
                        pPackedLight,
                        LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0)
                ), BrModelLoader.getModel(TestCarChassisPart.PART_MODEL), infos,
                new BrModelTextures.TwoSideInfoMap(new HashMap<>()),
                new ModelRenderVisitorList(List.of(BuiltInBrModelRenderVisitors.BLANK.get())));
        pPoseStack.popPose();
    }
}
