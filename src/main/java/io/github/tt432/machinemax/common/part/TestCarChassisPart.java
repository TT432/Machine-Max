package io.github.tt432.machinemax.common.part;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.part.slot.*;
import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.math.DVector3C;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;
import io.github.tt432.machinemax.utils.physics.ode.internal.DxGeom;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public class TestCarChassisPart extends AbstractPart {
    //模型资源参数
    public static final ResourceLocation PART_TEXTURE= ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID,"textures/entity/mini_ev.png");
    public static final ResourceLocation PART_MODEL = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID,"entity/mini_ev/mini_ev_chassis");
    public static final ResourceLocation PART_ANIMATION = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID,"entity/mini_ev/mini_ev.animation");
    public static final ResourceLocation PART_ANI_CONTROLLER = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID,"entity/mini_ev/mini_ev.animation_controllers");
    //属性参数
    public static final double BASIC_HEALTH = 20;
    public static final double BASIC_ARMOR = 1;
    //物理参数
    public static final double BASIC_MASS=400;
    public static final DVector3C airDragCentre=new DVector3(0,0,-0.1);//空气阻力/升力作用点(相对重心位置)
    public static final DVector3C waterDragCentre=new DVector3(0,0,-0.1);//水阻力/升力作用点(相对重心位置)

    public TestCarChassisPart(BasicEntity attachedEntity){
        super(attachedEntity);
        //模块化参数
        PART_TYPE = partTypes.CORE;
        PART_SLOT_NUM=5;
        MOD_SLOT_NUM=4;
        attach_point = new DVector3(0,0,0);
        this.children_parts=new ArrayList<>(PART_SLOT_NUM);
        this.modules=new ArrayList<>(MOD_SLOT_NUM);
        for(int i = 0; i < PART_SLOT_NUM; i++){//为车架部件设置部件安装槽
            this.children_parts.add(new UndefinedPartSlot(this));//占位的槽位类型，否则无法get/set
            switch(i){
                case 0://左前轮
                    this.children_parts.set(0,new SteeringWheelPartSlot(this,"left_front_wheel",50000,1000));
                    this.children_parts.get(0).setChildPartAttachPos(new DVector3(-17.0569/16, 0.7075/16, -19.0756/16));
                    this.children_parts.get(0).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,180,0));
                    this.children_parts.get(0).attachPart(new TestCarWheelPart(this.getAttachedEntity()));
                    break;
                case 1://左后轮
                    this.children_parts.set(1,new SteeringWheelPartSlot(this,"left_back_wheel",50000,1000));
                    this.children_parts.get(1).setChildPartAttachPos(new DVector3(-17.0569/16, 0.7075/16, 26.9244/16));
                    this.children_parts.get(1).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,180,0));
                    this.children_parts.get(1).attachPart(new TestCarWheelPart(this.getAttachedEntity()));
                    break;
                case 2://右后轮
                    this.children_parts.set(2,new SteeringWheelPartSlot(this,"right_back_wheel",50000,1000));
                    this.children_parts.get(2).setChildPartAttachPos(new DVector3(17.0569/16, 0.7075/16, 26.9244/16));
                    this.children_parts.get(2).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,0,0));
                    this.children_parts.get(2).attachPart(new TestCarWheelPart(this.getAttachedEntity()));
                    break;
                case 3://右前轮
                    this.children_parts.set(3,new SteeringWheelPartSlot(this,"right_front_wheel",50000,1000));
                    this.children_parts.get(3).setChildPartAttachPos(new DVector3(17.0569/16, 0.7075/16, -19.0756/16));
                    this.children_parts.get(3).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,0,0));
                    this.children_parts.get(3).attachPart(new TestCarWheelPart(this.getAttachedEntity()));
                    break;
                case 4://车壳
                    this.children_parts.set(4,new HullPartSlot(this, "hull"));
                    this.children_parts.get(4).setChildPartAttachPos(new DVector3(0, 10D/16, 4D/16));
                    this.children_parts.get(4).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,0,0));
                    this.children_parts.get(4).attachPart(new TestCarHullPart(this.getAttachedEntity()));
                    break;
                default:
                    break;//什么也不做
            }
        }
        //构建物理碰撞模型
        dmass.setBoxTotal(BASIC_MASS,40D/16,6D/16,72D/16);
        dbody.setMass(dmass);

        dgeoms=new DxGeom[1];

        dgeoms[0]=OdeHelper.createBox(40D/16,6D/16,72D/16);
        //dgeoms[0]=OdeHelper.createBox(40D/16,35D/16,72D/16);
        dgeoms[0].setBody(dbody);
        dgeoms[0].setOffsetPosition(0,(3.5D)/16,4D/16);//对齐碰撞体形状与模型形状
        //dgeoms[0].setOffsetPosition(0,(3.5D+35D/2)/16,-4D/16);//对齐碰撞体形状与模型形状
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
