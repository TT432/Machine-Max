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
import io.github.tt432.machinemax.common.entity.entity.TestCarEntity;
import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.List;

public class TestCarEntityRenderer extends EntityRenderer {

    private static final ResourceLocation TEST_CAR_TEXTURE = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID,"textures/entity/ae86.png");
    private static final ResourceLocation TEST_CAR_MODEL = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID,"entity/ae86");
    private static final ResourceLocation TEST_CAR_ANIMATION = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID,"entity/ae86.animation");
    private static final ResourceLocation TEST_CAR_ANI_CONTROLLER = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID,"entity/ae86.animation_controllers");

    protected TestCarEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Entity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight){
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        DVector3 heading = ((TestCarEntity)pEntity).CORE_PART.dbody.getQuaternion().toEulerDegrees();
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YN.rotationDegrees((float)heading.get1()));//将模型朝向与实体朝向相匹配
        pPoseStack.mulPose(Axis.XP.rotationDegrees((float)heading.get0()));//俯仰
        pPoseStack.mulPose(Axis.ZP.rotationDegrees((float)heading.get2()));//滚转
        RenderType renderType = RenderType.entitySolid(TEST_CAR_TEXTURE);
        AnimationComponent animationComponent = new AnimationComponent();
        animationComponent.setup(TEST_CAR_ANI_CONTROLLER, TEST_CAR_ANIMATION);
        var infos = BrAnimator.tickAnimation(animationComponent,
                RenderData.getComponent(pEntity).getScope(), ClientTickHandler.getTick() + pPartialTick);
        ModelRenderer.render(new RenderParams(
                pEntity,
                pPoseStack.last().copy(),
                pPoseStack,
                renderType,
                pBuffer.getBuffer(renderType),
                pPackedLight,
                OverlayTexture.NO_OVERLAY),//控制受伤变红与tnt爆炸前闪烁，载具不需要这个
                BrModelLoader.getModel(TEST_CAR_MODEL),
                infos,
                new BrModelTextures.TwoSideInfoMap(new HashMap<>()),
                new ModelRenderVisitorList(List.of(BuiltInBrModelRenderVisitors.BLANK.get())));
        //TODO:参考万灵药Mod的狐狸尾巴，写出一个通用的模组实体渲染器，需要通过Layer实现对安装零部件的显示
        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return TEST_CAR_TEXTURE;
    }
}
