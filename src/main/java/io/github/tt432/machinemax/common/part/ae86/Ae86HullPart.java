package io.github.tt432.machinemax.common.part.ae86;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.util.physics.math.DVector3;
import io.github.tt432.machinemax.util.physics.math.DVector3C;
import io.github.tt432.machinemax.util.physics.ode.OdeHelper;
import io.github.tt432.machinemax.util.physics.ode.internal.DxGeom;
import net.minecraft.resources.ResourceLocation;

public class Ae86HullPart extends AbstractPart {
    //模型资源参数
    public static final ResourceLocation PART_TEXTURE = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "textures/entity/ae86.png");
    public static final ResourceLocation PART_MODEL = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/ae86/ae86_hull");
    public static final ResourceLocation PART_ANIMATION = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/ae86/ae86_hull.animation");
    public static final ResourceLocation PART_ANI_CONTROLLER = ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "entity/ae86/ae86_hull.animation_controllers");
    //属性参数
    public static final double BASIC_HEALTH = 20;
    public static final double BASIC_ARMOR = 1;
    //物理参数
    public static final double BASIC_MASS = 800;
    public static final DVector3C airDragCentre = new DVector3(-0.1, 0, 0);//空气阻力/升力作用点(相对重心位置)
    public static final DVector3C waterDragCentre = new DVector3(-0.1, 0, 0);//水阻力/升力作用点(相对重心位置)

    public Ae86HullPart(BasicEntity attachedEntity) {
        super(attachedEntity);
        //模块化参数
        PART_TYPE = partTypes.HULL;
        //构建物理碰撞模型
        dmass.setBoxTotal(BASIC_MASS, 40D / 16, 32D / 16, 72D / 16);
        dbody.setMass(dmass);
        dgeoms = new DxGeom[2];

        dgeoms[0] = OdeHelper.createBox(40D / 16, 16D / 16, 65D / 16);
        dgeoms[0].setBody(dbody);
        dgeoms[0].setOffsetPosition(0, 1D / 16, -13.5D / 16);//对齐碰撞体形状与模型形状

        dgeoms[1] = OdeHelper.createBox(33D / 16, 16D / 16, 27D / 16);
        dgeoms[1].setBody(dbody);
        dgeoms[1].setOffsetPosition(0, 8.8576D / 16, -7.5674D / 16);//对齐碰撞体形状与模型形状
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
    public double getBasicArmor() {
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

    @Override
    public DVector3 getAerodynamicForceCoef(AbstractPart part) {
        //气动力相关系数
        double BASIC_AIRDRAG_COEF_ZP=0.5;//空气阻力系数(前向)，一般较小
        double BASIC_AIRDRAG_COEF_ZN=1;//空气阻力系数(后向)，一般较小
        double BASIC_AIRDRAG_COEF_XP=2;//空气阻力系数(左向)
        double BASIC_AIRDRAG_COEF_XN=2;//空气阻力系数(右向)
        double BASIC_AIRDRAG_COEF_YP=2;//空气阻力系数(上向)
        double BASIC_AIRDRAG_COEF_YN=0;//空气阻力系数(下向)
        double BASIC_AIRLIFT_COEF_Z =0.1;//空气升力系数(前向)，形状带来的额外升力
        double BASIC_AIRLIFT_COEF_X =0;//空气升力系数(水平向)，一般为0
        double BASIC_AIRLIFT_COEF_Y =0;//空气升力系数(垂向)，一般为0
        DVector3 coef=new DVector3(BASIC_AIRLIFT_COEF_X,BASIC_AIRLIFT_COEF_Y,BASIC_AIRLIFT_COEF_Z);
        DVector3 vAbs = new DVector3();
        part.dbody.getRelPointVel(part.airDragCentre, vAbs);//获取升力作用点的绝对速度
        DVector3 vRel = new DVector3();
        part.dbody.vectorFromWorld(vAbs, vRel);//绝对速度转换为相对速度
        if(vRel.get0()>0){
            coef.add0(BASIC_AIRDRAG_COEF_XP);
        } else {
            coef.add0(BASIC_AIRDRAG_COEF_XN);
        }
        if(vRel.get1()>0){
            coef.add1(BASIC_AIRDRAG_COEF_YP);
        } else {
            coef.add1(BASIC_AIRDRAG_COEF_YN);
        }
        if(vRel.get2()>0){
            coef.add2(BASIC_AIRDRAG_COEF_ZP);
        } else {
            coef.add2(BASIC_AIRDRAG_COEF_ZN);
        }
        return coef;
    }
}
