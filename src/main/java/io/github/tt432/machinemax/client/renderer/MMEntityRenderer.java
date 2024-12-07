package io.github.tt432.machinemax.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.tt432.eyelib.Eyelib;
import io.github.tt432.eyelib.capability.RenderData;
import io.github.tt432.eyelib.capability.component.AnimationComponent;
import io.github.tt432.eyelib.client.ClientTickHandler;
import io.github.tt432.eyelib.client.animation.BrAnimator;
import io.github.tt432.eyelib.client.loader.BrModelLoader;
import io.github.tt432.eyelib.client.render.RenderParams;
import io.github.tt432.eyelib.client.render.bone.BoneRenderInfos;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.util.physics.math.DQuaternionC;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class MMEntityRenderer extends EntityRenderer<BasicEntity> {

    protected MMEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BasicEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        if (pEntity.corePart == null) return;
        DQuaternionC dq = pEntity.corePart.dbody.getQuaternion().copy();
        Quaternionf q = new Quaternionf(dq.get1(), dq.get2(), dq.get3(), dq.get0());
        pPoseStack.pushPose();
        pPoseStack.mulPose(q);
        RenderType renderType;
        AnimationComponent animationComponent;
        BoneRenderInfos infos;
        RenderParams renderParams;
        for (AbstractPart part : pEntity.corePart) {//遍历根部件及其所有子孙部件
            renderType = RenderType.entitySolid(part.getTexture());
            animationComponent = RenderData.getComponent(pEntity).getAnimationComponent();
            animationComponent.setup(part.getAniController(), part.getAnimation());
            infos = BrAnimator.tickAnimation(animationComponent,
                    RenderData.getComponent(pEntity).getScope(), ClientTickHandler.getTick() + pPartialTick);
            renderParams = new RenderParams(//渲染参数
                    pEntity,
                    pPoseStack.last().copy(),
                    pPoseStack,
                    renderType,
                    part.getTexture(),
                    true,
                    pBuffer.getBuffer(renderType),
                    pPackedLight,
                    OverlayTexture.NO_OVERLAY//控制受伤变红与tnt爆炸前闪烁，载具不需要这个
            );
            if (part == pEntity.corePart) {//渲染根部件
                part.renderHelper = Eyelib.getRenderHelper().render(
                        renderParams,
                        BrModelLoader.getModel(pEntity.corePart.getModel()),
                        infos
                );
            } else {//渲染子部件
                part.fatherPart.renderHelper.renderOnLocator(
                        renderParams,
                        part.attachedSlot.locatorName, //在子部件所连接的槽位的对应locator处
                        BrModelLoader.getModel(part.getModel()), //渲染子部件模型
                        infos
                );
            }
        }
        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BasicEntity entity) {
        return entity.corePart.getTexture();
    }
}
