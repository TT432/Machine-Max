package io.github.tt432.machinemax.common.part;

import io.github.tt432.machinemax.util.physics.math.DVector3;

public interface IPartPhysParameters {
    /**
     * 根据运动体的相对运动状态，计算总的气动力系数
     * @return 运动体三个轴向的气动力系数
     */
    default DVector3 getAerodynamicForceCoef(AbstractPart part) {
        //气动力相关系数
        double BASIC_AIRDRAG_COEF_ZP=0.01;//空气阻力系数(前向)，一般较小
        double BASIC_AIRDRAG_COEF_ZN=0.01;//空气阻力系数(后向)，一般较小
        double BASIC_AIRDRAG_COEF_XP=0.01;//空气阻力系数(左向)
        double BASIC_AIRDRAG_COEF_XN=0.01;//空气阻力系数(右向)
        double BASIC_AIRDRAG_COEF_YP=0.01;//空气阻力系数(上向)
        double BASIC_AIRDRAG_COEF_YN=0.01;//空气阻力系数(下向)
        double BASIC_AIRLIFT_COEF_Z =0;//空气升力系数(前向)，形状带来的额外升力
        double BASIC_AIRLIFT_COEF_X =0;//空气升力系数(水平向)，一般为0
        double BASIC_AIRLIFT_COEF_Y =0;//空气升力系数(垂向)，一般为0
        //TODO:考虑旋转对称性
        DVector3 coef=new DVector3(BASIC_AIRLIFT_COEF_X,BASIC_AIRLIFT_COEF_Y,BASIC_AIRLIFT_COEF_Z);
        DVector3 vAbs = new DVector3();
        part.dbody.getRelPointVel(part.airDragCentre, vAbs);//获取升力作用点的绝对速度
        DVector3 vRel = new DVector3();
        part.dbody.vectorFromWorld(vAbs, vRel);//绝对速度转换为相对速度
        if(vRel.get0()>0){//x轴
            coef.add0(BASIC_AIRDRAG_COEF_XP);
        } else {
            coef.add0(BASIC_AIRDRAG_COEF_XN);
        }
        if(vRel.get1()>0){//y轴
            coef.add1(BASIC_AIRDRAG_COEF_YP);
        } else {
            coef.add1(BASIC_AIRDRAG_COEF_YN);
        }
        if(vRel.get2()>0){//z轴
            coef.add2(BASIC_AIRDRAG_COEF_ZP);
        } else {
            coef.add2(BASIC_AIRDRAG_COEF_ZN);
        }
        return coef;
    }
    /**
     * 根据运动体的相对运动状态，计算总的水动力系数
     * @return 运动体三个轴向的水动力系数
     */
    default DVector3 getHydrodynamicForceCoef(AbstractPart part) {
        //水动力相关系数
        double BASIC_WATERDRAG_COEF_ZP=1000*800;//水阻力系数(前向)
        double BASIC_WATERDRAG_COEF_ZN=1000*800;//水阻力系数(后向)
        double BASIC_WATERDRAG_COEF_XP=1000*800;//水阻力系数(左向)
        double BASIC_WATERDRAG_COEF_XN=1000*800;//水阻力系数(右向)
        double BASIC_WATERDRAG_COEF_YP=1000*800;//水阻力系数(上向)
        double BASIC_WATERDRAG_COEF_YN=1000*800;//水阻力系数(下向)
        double BASIC_WATERLIFT_COEF_Z =0;//水升力系数(前向)
        double BASIC_WATERLIFT_COEF_X =0;//水升力系数(水平向)
        double BASIC_WATERLIFT_COEF_Y =0;//水升力系数(垂向)
        //TODO:考虑旋转对称性
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
