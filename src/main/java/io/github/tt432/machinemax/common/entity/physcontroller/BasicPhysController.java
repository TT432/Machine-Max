package io.github.tt432.machinemax.common.entity.physcontroller;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;
import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DQuaternionC;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.math.DVector3C;
import io.github.tt432.machinemax.utils.physics.ode.DRotation;
import io.github.tt432.machinemax.utils.physics.ode.internal.Rotation;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.Entity;

/**
 * 此类为实体的物理控制器原型
 * 这里应写有所有模组实体的物理运动逻辑(表面摩擦力除外，物理引擎自行处理了)
 * 此外还有一些实用的方法
 * @author 甜粽子
 */
public class BasicPhysController {

    @Getter
    @Setter
    protected BasicEntity controlledEntity;//此控制器控制的实体
    protected boolean posNeedsUpdate = false;//是否需要更新位置
    protected DVector3 posToApply;//新位置
    protected boolean rotNeedsUpdate = false;//是否需要更新姿态
    protected DQuaternion rotToApply;//新姿态

    public BasicPhysController(BasicEntity entity){
        this.controlledEntity=entity;
    }
    /**
     * 将力、力矩、速度、角速度、位置、姿态等数据施加于各个运动体
     */
    public void applyAllEffects(){
        //物理模拟线程将会每计算帧调用一次此方法，因此应在这里写入所有要附加于运动体的力与力矩
        if(posNeedsUpdate){
            applySetPosition();
            posNeedsUpdate=false;
        }
        if(rotNeedsUpdate){
            applySetRotation();
            rotNeedsUpdate=false;
        }
    }

    /**
     * 待物理线程未在处理运动和碰撞时，将实体传送至指定位置
     * @param x
     * @param y
     * @param z
     */
    public void setPositionEnqueue(double x, double y, double z){
        this.posToApply = new DVector3(x,y,z);
        posNeedsUpdate = true;
    }

    /**
     * 待物理线程未在处理运动和碰撞时，将实体传送至指定位置
     * @param v 实体的新位置坐标
     */
    public void setPositionEnqueue(DVector3C v){
        setPositionEnqueue(v.get0(),v.get1(),v.get2());
    }

    private void applySetPosition(){
        if(controlledEntity.CORE_PART!=null){
            DVector3 delta = new DVector3(posToApply).sub(controlledEntity.CORE_PART.dbody.getPosition());
            controlledEntity.CORE_PART.dbody.setPosition(posToApply);//将根部件移动到新位置
            for (AbstractMMPart part : controlledEntity.CORE_PART) {
                part.dbody.setPosition(part.dbody.getPosition().reAdd(delta));//根据坐标相对变化量，移动每个部件的位置
            }
        }
    }
    //WIP
    public void setRotationEnqueue(double pitch, double yaw, double roll){
        setRotationEnqueue(DQuaternion.fromEulerDegrees(pitch,yaw,roll));
    }
    //WIP
    public void setRotationEnqueue(DQuaternionC q){
        this.rotToApply = (DQuaternion) q;
        rotNeedsUpdate = true;
    }
    //WIP
    private void applySetRotation(){
        controlledEntity.CORE_PART.dbody.setQuaternion(rotToApply);

        //TODO:绕着整体的质心旋转实体而非各自的质心,左乘父部件旋转矩阵的逆即其转置，再左乘新的旋转矩阵？
    }

}
