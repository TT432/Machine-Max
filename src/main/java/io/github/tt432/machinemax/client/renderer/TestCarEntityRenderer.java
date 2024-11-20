package io.github.tt432.machinemax.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.tt432.eyelib.Eyelib;
import io.github.tt432.eyelib.capability.RenderData;
import io.github.tt432.eyelib.capability.component.AnimationComponent;
import io.github.tt432.eyelib.client.ClientTickHandler;
import io.github.tt432.eyelib.client.animation.BrAnimator;
import io.github.tt432.eyelib.client.loader.BrModelLoader;
import io.github.tt432.eyelib.client.render.RenderParams;
import io.github.tt432.eyelib.client.render.bone.BoneRenderInfos;
import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entity.TestCarEntity;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class TestCarEntityRenderer extends EntityRenderer<TestCarEntity> {

    static final ResourceLocation TEST_CAR_TEXTURE = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "textures/entity/mini_ev.png");
    static final ResourceLocation TEST_CAR_MODEL = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/mini_ev/mini_ev");
    static final ResourceLocation TEST_CAR_MODEL2 = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/mini_ev/mini_ev_chassis");
    static final ResourceLocation TEST_CAR_MODEL3 = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/mini_ev/mini_ev_wheel");
    static final ResourceLocation TEST_CAR_ANIMATION = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/mini_ev.animation");
    static final ResourceLocation TEST_CAR_ANI_CONTROLLER = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/mini_ev.animation_controllers");

    protected TestCarEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TestCarEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        if (pEntity.corePart == null) return;
        DVector3 heading = pEntity.corePart.dbody.getQuaternion().toEulerDegrees();
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YN.rotationDegrees((float) heading.get1()));//将模型朝向与实体朝向相匹配
        pPoseStack.mulPose(Axis.XP.rotationDegrees((float) heading.get0()));//俯仰
        pPoseStack.mulPose(Axis.ZP.rotationDegrees((float) heading.get2()));//滚转
        RenderType renderType = RenderType.entitySolid(TEST_CAR_TEXTURE);
        AnimationComponent animationComponent = RenderData.getComponent(pEntity).getAnimationComponent();
        animationComponent.setup(TEST_CAR_ANI_CONTROLLER, TEST_CAR_ANIMATION);
        var infos = BrAnimator.tickAnimation(animationComponent,
                RenderData.getComponent(pEntity).getScope(), ClientTickHandler.getTick() + pPartialTick);
        RenderParams renderParams = new RenderParams(
                pEntity,
                pPoseStack.last().copy(),
                pPoseStack,
                renderType,
                TEST_CAR_TEXTURE,
                true,
                pBuffer.getBuffer(renderType),
                pPackedLight,
                OverlayTexture.NO_OVERLAY//控制受伤变红与tnt爆炸前闪烁，载具不需要这个
        );
        Eyelib.getRenderHelper().render(renderParams, BrModelLoader.getModel(TEST_CAR_MODEL2), infos)
                .renderOnLocator("wheel", BrModelLoader.getModel(TEST_CAR_MODEL3), BoneRenderInfos.EMPTY);
        //TODO:参考万灵药Mod的狐狸尾巴，写出一个通用的模组实体渲染器，需要通过Layer实现对安装零部件的显示
        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(TestCarEntity entity) {
        return TEST_CAR_TEXTURE;
    }
}
