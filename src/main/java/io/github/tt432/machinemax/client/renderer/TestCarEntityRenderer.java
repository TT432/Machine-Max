package io.github.tt432.machinemax.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;

public class TestCarEntityRenderer extends EntityRenderer {

    private static final ResourceLocation TEST_CAR_TEXTURE = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "textures/entity/testcar.png");
    private static final ResourceLocation TEST_CAR_MODEL = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "bedrock_models/entity/testcar.json");

    protected TestCarEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Entity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight){
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.pushPose();
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
                0),
                BrModelLoader.getModel(TEST_CAR_MODEL),infos,
                new BrModelTextures.TwoSideInfoMap(new HashMap<>()),
                new ModelRenderVisitorList(List.of(BuiltInBrModelRenderVisitors.BLANK.get())));
        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return TEST_CAR_TEXTURE;
    }
}
