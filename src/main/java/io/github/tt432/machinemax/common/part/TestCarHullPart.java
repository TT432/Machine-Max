package io.github.tt432.machinemax.common.part;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.math.DVector3C;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;
import io.github.tt432.machinemax.utils.physics.ode.internal.DxGeom;
import net.minecraft.resources.ResourceLocation;

public class TestCarHullPart extends AbstractPart {
    //模型资源参数
//    public static final ResourceLocation PART_TEXTURE = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "textures/entity/mini_ev.png");
//    public static final ResourceLocation PART_MODEL = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/mini_ev/mini_ev_hull");
    public static final ResourceLocation PART_TEXTURE = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "textures/entity/kv2.png");
    public static final ResourceLocation PART_MODEL = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/kv2/kv2_turret");
    public static final ResourceLocation PART_ANIMATION = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/mini_ev/mini_ev.animation");
    public static final ResourceLocation PART_ANI_CONTROLLER = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/mini_ev/mini_ev.animation_controllers");
    //属性参数
    public static final double BASIC_HEALTH = 20;
    public static final double BASIC_ARMOR = 1;
    //物理参数
    public static final double BASIC_MASS = 800;
    public static final DVector3C airDragCentre = new DVector3(-0.1, 0, 0);//空气阻力/升力作用点(相对重心位置)
    public static final DVector3C waterDragCentre = new DVector3(-0.1, 0, 0);//水阻力/升力作用点(相对重心位置)

    public TestCarHullPart(BasicEntity attachedEntity) {
        super(attachedEntity);
        //模块化参数
        PART_TYPE = partTypes.HULL;
        //构建物理碰撞模型
        dmass.setBoxTotal(BASIC_MASS, 40D / 16, 32D / 16, 72D / 16);
        dbody.setMass(dmass);
        dgeoms = new DxGeom[2];

        dgeoms[0] = OdeHelper.createBox(40D / 16, 21D / 16, 72D / 16);
        dgeoms[0].setBody(dbody);
        dgeoms[0].setOffsetPosition(0, 3D / 16, -4D / 16);//对齐碰撞体形状与模型形状

        dgeoms[1] = OdeHelper.createBox(40D / 16, 14D / 16, 50D / 16);
        dgeoms[1].setBody(dbody);
        dgeoms[1].setOffsetPosition(0, 20.5D / 16, -14D / 16);//对齐碰撞体形状与模型形状
    }

    @Override
    public double getMass() {
        return BASIC_MASS;
    }

    @Override
    public void updateMass() {
        dmass.adjust(getMass());
    }

    @Override
    public double getArmor() {
        return BASIC_ARMOR;
    }

    @Override
    public double getMaxHealth() {
        return BASIC_HEALTH;
    }

    @Override
    public ResourceLocation getModel() {
        return PART_MODEL;
    }

    @Override
    public ResourceLocation getTexture() {
        return PART_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimation() {
        return PART_ANIMATION;
    }

    @Override
    public ResourceLocation getAniController() {
        return PART_ANI_CONTROLLER;
    }
}
