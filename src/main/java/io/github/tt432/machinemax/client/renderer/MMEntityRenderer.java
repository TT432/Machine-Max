package io.github.tt432.machinemax.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.tt432.eyelib.Eyelib;
import io.github.tt432.eyelib.capability.RenderData;
import io.github.tt432.eyelib.capability.component.AnimationComponent;
import io.github.tt432.eyelib.client.ClientTickHandler;
import io.github.tt432.eyelib.client.animation.BrAnimator;
import io.github.tt432.eyelib.client.loader.BrModelLoader;
import io.github.tt432.eyelib.client.render.ModelRenderer;
import io.github.tt432.eyelib.client.render.RenderHelper;
import io.github.tt432.eyelib.client.render.RenderParams;
import io.github.tt432.eyelib.client.render.bone.BoneRenderInfos;
import io.github.tt432.eyelib.client.render.visitor.BuiltInBrModelRenderVisitors;
import io.github.tt432.eyelib.client.render.visitor.ModelRenderVisitorList;
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

import java.util.List;

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
        pPoseStack.pushPose();//开始渲染
        pPoseStack.mulPose(q);
        RenderType renderType;
        AnimationComponent animationComponent;
        BoneRenderInfos infos;
        RenderParams renderParams;
        RenderHelper renderHelper = Eyelib.getRenderHelper();
        for (AbstractPart part : pEntity.corePart) {//遍历根部件及其所有子孙部件
            renderType = RenderType.entitySolid(part.getTexture());
            animationComponent = RenderData.getComponent(pEntity).getAnimationComponent();//获取已有的动画数据
            animationComponent.setup(part.getAniController(),part.getAnimation());
            //问题：一个实体的RenderData的getAnimationComponent似乎只能包含一个动画和控制器文件中的内容？
            //需求：同一实体，不同部件能够使用不同的动画和控制器，且动画控制器和动画跟随安装部件的变化而变化。
            //现在的实现：根据部件存储的ResourceLocation，每次渲染时setup各个部件的动画，但这会导致动画停留在0秒时刻，不播放。
            //TODO:可能的解决方案：给各个零部件attach一个RenderData，上面读取时读part的而不是实体的？
            infos = BrAnimator.tickAnimation(animationComponent,
                    part.molangScope.getScope(), ClientTickHandler.getTick() + pPartialTick);
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
                renderHelper.render(
                        renderParams,
                        BrModelLoader.getModel(pEntity.corePart.getModel()),
                        infos
                );
            } else {//渲染子部件
                renderHelper.renderOnLocator(
                        renderParams,
                        part.attachedSlot.locatorName, //在子部件所连接的槽位的对应locator处
                        BrModelLoader.getModel(part.getModel()), //渲染子部件模型
                        infos
                );
            }
        }
        pPoseStack.popPose();//结束渲染
    }

    @Override
    public ResourceLocation getTextureLocation(BasicEntity entity) {
        return entity.corePart.getTexture();
    }

}
