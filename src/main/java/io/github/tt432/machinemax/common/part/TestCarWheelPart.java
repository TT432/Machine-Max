package io.github.tt432.machinemax.common.part;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.math.DVector3C;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;
import io.github.tt432.machinemax.utils.physics.ode.internal.DxGeom;
import net.minecraft.resources.ResourceLocation;

public class TestCarWheelPart extends AbstractWheelPart {
    //模型资源参数
    public static final ResourceLocation PART_TEXTURE = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "textures/entity/mini_ev.png");
    public static final ResourceLocation PART_MODEL = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/mini_ev/mini_ev_wheel");
    public static final ResourceLocation PART_ANIMATION = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/mini_ev/mini_ev_wheel.animation");
    public static final ResourceLocation PART_ANI_CONTROLLER = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/mini_ev/mini_ev_wheel.animation_controllers");
    //属性参数
    public static final double BASIC_HEALTH = 20;
    public static final double BASIC_ARMOR = 1;
    //物理参数
    public static final double BASIC_MASS = 30;
    public static final DVector3C airDragCentre = new DVector3(-0.1, 0, 0);//空气阻力/升力作用点(相对重心位置)
    public static final DVector3C waterDragCentre = new DVector3(-0.1, 0, 0);//水阻力/升力作用点(相对重心位置)

    public TestCarWheelPart(BasicEntity entity) {
        super(entity);
        //几何参数
        WHEEL_RADIUS = 13.25D / 2 / 16;
        WHEEL_WIDTH = 4.25D / 16;
        //模块化参数
        PART_TYPE = partTypes.WHEEL;
        //构建物理碰撞模型
        dmass.setCylinderTotal(BASIC_MASS, 1, WHEEL_RADIUS, WHEEL_WIDTH);
        dbody.setMass(dmass);

        dgeoms = new DxGeom[1];

        dgeoms[0] = OdeHelper.createCylinder(WHEEL_RADIUS, WHEEL_WIDTH);//创建一个平面朝向Z正方向的圆柱体
        dgeoms[0].setBody(dbody);//碰撞体绑定到运动体
        dgeoms[0].setOffsetQuaternion(DQuaternion.fromEulerDegrees(0, 90, 0));//默认创建的是一个立着的圆柱，转一下
        dgeoms[0].setOffsetPosition(0.8D / 16, 0, 0);//对齐碰撞体形状与模型形状
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
