package io.github.tt432.machinemax.common.entity.part;

import io.github.tt432.machinemax.utils.physics.math.DVector3;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public interface MMPartPhysParametersC {
    /**
     * 根据运动体的相对运动状态，计算总的气动力系数
     * @return 运动体三个轴向的气动力系数
     */
    default public DVector3 getAerodynamicForceCoef(AbstractMMPart part) {
        //气动力相关系数
        double BASIC_AIRDRAG_COEF_ZP=1;//空气阻力系数(前向)，一般较小
        double BASIC_AIRDRAG_COEF_ZN=1;//空气阻力系数(后向)，一般较小
        double BASIC_AIRDRAG_COEF_XP=1;//空气阻力系数(左向)
        double BASIC_AIRDRAG_COEF_XN=1;//空气阻力系数(右向)
        double BASIC_AIRDRAG_COEF_YP=1;//空气阻力系数(上向)
        double BASIC_AIRDRAG_COEF_YN=1;//空气阻力系数(下向)
        double BASIC_AIRLIFT_COEF_Z =0;//空气升力系数(前向)，形状带来的额外升力
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
    static AttributeSupplier.Builder createLivingAttributes() {
        return AttributeSupplier.builder()
                .add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED)
                .add(Attributes.ARMOR)
                .add(Attributes.ARMOR_TOUGHNESS)
                .add(Attributes.MAX_ABSORPTION)
                .add(Attributes.STEP_HEIGHT,0)
                .add(Attributes.SCALE)
                .add(Attributes.GRAVITY,0)//屏蔽原版重力，交由物理引擎控制
                .add(Attributes.SAFE_FALL_DISTANCE)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER,0)//屏蔽原版掉落伤害
                .add(Attributes.JUMP_STRENGTH)
                .add(Attributes.OXYGEN_BONUS)
                .add(Attributes.BURNING_TIME)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY)
                .add(Attributes.MOVEMENT_EFFICIENCY)
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(net.neoforged.neoforge.common.NeoForgeMod.SWIM_SPEED)
                .add(net.neoforged.neoforge.common.NeoForgeMod.NAMETAG_DISTANCE);
    }
    /**
     * 根据运动体的相对运动状态，计算总的水动力系数
     * @return 运动体三个轴向的水动力系数
     */
    default public DVector3 getHydrodynamicForceCoef(AbstractMMPart part) {
        //水动力相关系数
        double BASIC_WATERDRAG_COEF_ZP=1;//水阻力系数(前向)
        double BASIC_WATERDRAG_COEF_ZN=1;//水阻力系数(后向)
        double BASIC_WATERDRAG_COEF_XP=1;//水阻力系数(左向)
        double BASIC_WATERDRAG_COEF_XN=1;//水阻力系数(右向)
        double BASIC_WATERDRAG_COEF_YP=1;//水阻力系数(上向)
        double BASIC_WATERDRAG_COEF_YN=1;//水阻力系数(下向)
        double BASIC_WATERLIFT_COEF_Z =0;//水升力系数(前向)
        double BASIC_WATERLIFT_COEF_X =0;//水升力系数(水平向)
        double BASIC_WATERLIFT_COEF_Y =0;//水升力系数(垂向)
        DVector3 coef=new DVector3(BASIC_WATERLIFT_COEF_X,BASIC_WATERLIFT_COEF_Y,BASIC_WATERLIFT_COEF_Z);
        DVector3 vAbs = new DVector3();
        part.dbody.getRelPointVel(part.waterDragCentre, vAbs);//获取升力作用点的绝对速度
        DVector3 vRel = new DVector3();
        part.dbody.vectorFromWorld(vAbs, vRel);//绝对速度转换为相对速度
        if(vRel.get0()>0){
            coef.add0(BASIC_WATERDRAG_COEF_XP);
        } else {
            coef.add0(BASIC_WATERDRAG_COEF_XN);
        }
        if(vRel.get1()>0){
            coef.add1(BASIC_WATERDRAG_COEF_YP);
        } else {
            coef.add1(BASIC_WATERDRAG_COEF_YN);
        }
        if(vRel.get2()>0){
            coef.add2(BASIC_WATERDRAG_COEF_ZP);
        } else {
            coef.add2(BASIC_WATERDRAG_COEF_ZN);
        }
        return coef;
    }
}
