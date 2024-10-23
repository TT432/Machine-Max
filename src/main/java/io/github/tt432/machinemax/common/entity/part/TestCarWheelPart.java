package io.github.tt432.machinemax.common.entity.part;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;
import io.github.tt432.machinemax.utils.physics.ode.internal.DxGeom;

public class TestCarWheelPart extends AbstractWheelPart{
    public TestCarWheelPart(BasicEntity entity) {
        super(entity);
        //几何参数
        WHEEL_RADIUS = 13.25D/16;
        WHEEL_WIDTH  = 4.25D/16;
        //模块化参数
        PART_SLOT_NUM=4;
        MOD_SLOT_NUM=0;
        //物理参数
        BASIC_MASS = 5;
        attach_point = new DVector3(0,0,0);
        BASIC_AIRDRAG_COEF_ZP=0;
        BASIC_AIRDRAG_COEF_ZN=0;
        BASIC_AIRDRAG_COEF_XP=0;
        BASIC_AIRDRAG_COEF_XN=0;
        BASIC_AIRDRAG_COEF_YP=0;
        BASIC_AIRDRAG_COEF_YN=0;
        BASIC_AIRLIFT_COEF_Z=0;
        BASIC_AIRLIFT_COEF_X=0;
        BASIC_AIRLIFT_COEF_Y=0;
        AIRDRAG_CENTRE=new DVector3(0,0,0);
        BASIC_WATERDRAG_COEF_ZP=0;
        BASIC_WATERDRAG_COEF_ZN=0;
        BASIC_WATERDRAG_COEF_XP=0;
        BASIC_WATERDRAG_COEF_XN=0;
        BASIC_WATERDRAG_COEF_YP=0;
        BASIC_WATERDRAG_COEF_YN=0;
        BASIC_WATERLIFT_COEF_Z=0;
        BASIC_WATERLIFT_COEF_X=0;
        BASIC_WATERLIFT_COEF_Y=0;
        AIRDRAG_CENTRE=new DVector3(0,0,0);
        //构建物理碰撞模型
        dmass.setCylinderTotal(getMass(),1,WHEEL_RADIUS,WHEEL_WIDTH);
        dbody.setMass(dmass);

        dgeoms=new DxGeom[1];

        dgeoms[0]=OdeHelper.createCylinder(WHEEL_RADIUS,WHEEL_WIDTH);
        dgeoms[0].setBody(dbody);
        dgeoms[0].setOffsetQuaternion(DQuaternion.fromEulerDegrees(0,0,90));
        dgeoms[0].setOffsetPosition(0.8D/16,0,0);//对齐碰撞体形状与模型形状
    }
}
