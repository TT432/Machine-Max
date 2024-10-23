package io.github.tt432.machinemax.common.entity.part;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;

public class TestCarWheelPart extends AbstractWheelPart{
    public TestCarWheelPart(BasicEntity entity) {
        super(entity);
        WHEEL_RADIUS = 13.25D/16;
        WHEEL_WIDTH  = 4.25D/16;
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
        dmass.setCylinderTotal(getMass(),1,WHEEL_RADIUS,WHEEL_WIDTH);
        dbody.setMass(dmass);
        dgeom= OdeHelper.createCylinder(WHEEL_RADIUS,WHEEL_WIDTH);
        dgeom.setBody(dbody);
        dgeom.setOffsetPosition(0.8D/16,0,0);//对齐碰撞体形状与模型形状
    }
}
