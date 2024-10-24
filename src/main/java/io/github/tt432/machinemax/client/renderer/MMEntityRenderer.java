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
import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;

/**
 * 本模组实体的实际模型通过Layer的方式渲染，这里仅根据实体对应核心运动体的姿态渲染模型整体的旋转
 */
public class MMEntityRenderer extends EntityRenderer {

    private static final ResourceLocation TEST_CUBE_TEXTURE = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID,"textures/entity/cube.png");
    private static final ResourceLocation TEST_CUBE_MODEL = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID,"entity/cube");

    public MMEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Entity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        AbstractMMPart corePart =((BasicEntity)entity).CORE_PART;
        if(corePart!=null && corePart.dbody!=null){//若实体已指定部件，且部件已指定运动体，则根据运动体姿态控制实体旋转
            DVector3 heading = ((BasicEntity)entity).CORE_PART.dbody.getQuaternion().toEulerDegrees();
            poseStack.pushPose();
            poseStack.mulPose(Axis.YN.rotationDegrees((float)heading.get1()));//将模型朝向与实体朝向相匹配
            poseStack.mulPose(Axis.XP.rotationDegrees((float)heading.get0()));//俯仰
            poseStack.mulPose(Axis.ZP.rotationDegrees((float)heading.get2()));//滚转
            poseStack.popPose();
        }else {//否则渲染一个缺省模型
            poseStack.pushPose();
            RenderType renderType = RenderType.entitySolid(TEST_CUBE_TEXTURE);
            AnimationComponent animationComponent = new AnimationComponent();
            animationComponent.setup(TEST_CUBE_MODEL, TEST_CUBE_MODEL);
            var infos = BrAnimator.tickAnimation(animationComponent,
                    RenderData.getComponent(entity).getScope(), ClientTickHandler.getTick() + partialTick);
            ModelRenderer.render(new RenderParams(
                            entity,
                            poseStack.last().copy(),
                            poseStack,
                            renderType,
                            bufferSource.getBuffer(renderType),
                            packedLight,
                            OverlayTexture.NO_OVERLAY),//控制受伤变红与tnt爆炸前闪烁，载具不需要这个
                    BrModelLoader.getModel(TEST_CUBE_MODEL),
                    infos,
                    new BrModelTextures.TwoSideInfoMap(new HashMap<>()),
                    new ModelRenderVisitorList(List.of(BuiltInBrModelRenderVisitors.BLANK.get())));
            poseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return TEST_CUBE_TEXTURE;
    }
}
