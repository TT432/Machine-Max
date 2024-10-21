package io.github.tt432.machinemax.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.sun.jna.platform.win32.WinBase;
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
import io.github.tt432.eyelib.util.ResourceLocations;
import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.TestCarEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.List;

public class TestCarEntityRenderer extends EntityRenderer {

    private static final ResourceLocation TEST_CAR_TEXTURE = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID,"textures/entity/cube.png");
    private static final ResourceLocation TEST_CAR_MODEL = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID,"entity/cube");

    protected TestCarEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Entity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight){
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YN.rotationDegrees(pEntityYaw));//将模型朝向与实体朝向相匹配
        pPoseStack.mulPose(Axis.XP.rotationDegrees(pEntity.getXRot()));//俯仰
        //pPoseStack.mulPose(Axis.ZP.rotationDegrees(((TestCarEntity)pEntity).getZRot()));//滚转
        //pPoseStack.mulPose(((TestCarEntity)pEntity).q);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(-180));
        RenderType renderType = RenderType.entitySolid(TEST_CAR_TEXTURE);
        AnimationComponent animationComponent = new AnimationComponent();
        animationComponent.setup(TEST_CAR_MODEL, TEST_CAR_MODEL);
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
        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return TEST_CAR_TEXTURE;
    }
}
