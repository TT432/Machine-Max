package io.github.tt432.machinemax.common.entity.part;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.entity.part.slot.UndefinedModuleSlot;
import io.github.tt432.machinemax.common.entity.part.slot.UndefinedPartSlot;
import io.github.tt432.machinemax.common.entity.part.slot.WheelPartSlot;
import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.DGeom;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;
import io.github.tt432.machinemax.utils.physics.ode.internal.DxGeom;
import org.joml.Quaternionf;

import java.util.ArrayList;

public class TestCarChassisPart extends AbstractMMPart{

    public TestCarChassisPart(BasicEntity attachedEntity){
        super(attachedEntity);
        //模块化参数
        PART_TYPE = partTypes.CORE;
        PART_SLOT_NUM=4;
        MOD_SLOT_NUM=0;
        this.children_parts=new ArrayList<>(PART_SLOT_NUM);
        this.modules=new ArrayList<>(MOD_SLOT_NUM);
        for(int i = 0; i < PART_SLOT_NUM; i++){//为车架部件设置四个车轮安装槽
            this.children_parts.add(new UndefinedPartSlot());
            switch(i){
                case 0://右前轮
                    this.children_parts.set(0,new WheelPartSlot());
                    this.children_parts.get(0).setChildPartAttachPos(new DVector3(-17.4909/16, -9.91749/16, 23.0756/16));
                    this.children_parts.get(0).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,180,0));
                    break;
                case 1://左前轮
                    this.children_parts.set(1,new WheelPartSlot());
                    this.children_parts.get(1).setChildPartAttachPos(new DVector3(17.4909/16, -9.91749/16, 23.0756/16));
                    this.children_parts.get(1).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,0,0));
                    break;
                case 2://左后轮
                    this.children_parts.set(2,new WheelPartSlot());
                    this.children_parts.get(2).setChildPartAttachPos(new DVector3(17.4909/16, -9.91749/16, -22.9244/16));
                    this.children_parts.get(2).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,0,0));
                    break;
                case 3://右后轮
                    this.children_parts.set(3,new WheelPartSlot());
                    this.children_parts.get(3).setChildPartAttachPos(new DVector3(-17.4909/16, -9.91749/16, -22.9244/16));
                    this.children_parts.get(3).setChildPartAttachRot(DQuaternion.fromEulerDegrees(0,180,0));
                    break;
                default:
                    break;//什么也不做
            }
        }
        //物理参数
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
        //构建物理碰撞模型
        dmass.setBoxTotal(getMass(),40D/16,32D/16,72D/16);
        dbody.setMass(dmass);

        dgeoms=new DxGeom[2];

        dgeoms[0]=OdeHelper.createBox(40D/16,21D/16,72D/16);
        dgeoms[0].setBody(dbody);
        dgeoms[0].setOffsetPosition(0,8.5D/16,-12D/16);//对齐碰撞体形状与模型形状
        dgeoms[1]=OdeHelper.createBox(40D/16,14D/16,50D/16);
        dgeoms[1].setBody(dbody);
        dgeoms[1].setOffsetPosition(0,20.5D/16,-10D/16);//对齐碰撞体形状与模型形状
    }

}
