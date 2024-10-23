package io.github.tt432.machinemax.common.entity.part;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.entity.part.slot.WheelPartSlot;
import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;
import org.joml.Quaternionf;

public class TestCarChassisPart extends AbstractMMPart{

    static DVector3 SIZE = new DVector3(40D/16,32D/16,72D/16);

    public TestCarChassisPart(BasicEntity attachedEntity){
        super(attachedEntity);
        PART_TYPE = partTypes.CORE;

        for(int i = 0; i < 4; i++){//为车架部件设置四个车轮安装槽
            this.children_parts.set(i,new WheelPartSlot());
            switch(i){
                case 0://右前轮
                    this.children_parts.get(0).setChildPartAttachPos(new DVector3(-17.5/16,-9.9/16,23.1/16));
                    this.children_parts .get(0).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,180,0));
                    break;
                case 1://左前轮
                    this.children_parts.get(1).setChildPartAttachPos(new DVector3(17.5/16,-9.9/16,23.1/16));
                    this.children_parts .get(1).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,0,0));
                    break;
                case 2://左后轮
                    this.children_parts.get(2).setChildPartAttachPos(new DVector3(17.5/16,-9.9/16,-22.9/16));
                    this.children_parts .get(2).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,0,0));
                    break;
                case 3://右后轮
                    this.children_parts.get(3).setChildPartAttachPos(new DVector3(-17.5/16,-9.9/16,-22.9/16));
                    this.children_parts .get(3).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,180,0));
                    break;
                default:
                    break;//什么也不做
            }
        }

        BASIC_MASS=1200;
        BASIC_AIRDRAG_COEF_ZP=0.8;
        BASIC_AIRDRAG_COEF_ZN=2;
        BASIC_AIRDRAG_COEF_XP=2;
        BASIC_AIRDRAG_COEF_XN=1;
        BASIC_AIRDRAG_COEF_YP=2;
        BASIC_AIRDRAG_COEF_YN=2;
        BASIC_AIRLIFT_COEF_Z=0.1;
        BASIC_AIRLIFT_COEF_X=0;
        BASIC_AIRLIFT_COEF_Y=0;
        AIRDRAG_CENTRE=new DVector3(-0.1,0,0);
        BASIC_WATERDRAG_COEF_ZP=0.8;
        BASIC_WATERDRAG_COEF_ZN=2;
        BASIC_WATERDRAG_COEF_XP=2;
        BASIC_WATERDRAG_COEF_XN=1;
        BASIC_WATERDRAG_COEF_YP=2;
        BASIC_WATERDRAG_COEF_YN=2;
        BASIC_WATERLIFT_COEF_Z=0.02;
        BASIC_WATERLIFT_COEF_X=0;
        BASIC_WATERLIFT_COEF_Y=0;
        AIRDRAG_CENTRE=new DVector3(-0.1,0,0);

        dmass.setBoxTotal(getMass(),SIZE.get0(),SIZE.get1(),SIZE.get2());
        dbody.setMass(dmass);
        dgeom=OdeHelper.createBox(SIZE);
        dgeom.setBody(dbody);
        dgeom.setOffsetPosition(0,8D/16,-12D/16);//对齐碰撞体形状与模型形状

    }

}
