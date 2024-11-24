package io.github.tt432.machinemax.common.entity.controller;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.utils.physics.math.*;
import io.github.tt432.machinemax.utils.physics.ode.internal.Rotation;
import lombok.Getter;
import lombok.Setter;

/**
 * 此类为实体的控制器原型
 * <p>
 * 这里应写有所有模组实体的物理运动逻辑(表面摩擦力除外，物理引擎自行处理了)
 * <p>
 * 此外还有一些实用的方法
 *
 * @author 甜粽子
 */
public class PhysController {

    @Getter
    @Setter
    protected BasicEntity controlledEntity;//此控制器控制的实体
    @Setter
    protected byte[] rawMoveInput = new byte[6];//移动输入
    @Setter
    protected byte[] moveInputConflict = new byte[6];//移动输入冲突情况
    protected volatile boolean posNeedsUpdate = false;//是否需要更新位置
    protected DVector3 posToApply;//新位置
    protected volatile boolean lVelNeedsUpdate = false;//是否需要更新速度
    protected DVector3 lVelToApply;//新速度
    protected volatile boolean rotNeedsUpdate = false;//是否需要更新姿态
    protected DQuaternion rotToApply;//新姿态
    protected volatile boolean aVelNeedsUpdate = false;//是否需要更新角速度
    protected DVector3 aVelToApply;//新角速度

    public PhysController(BasicEntity entity) {
        this.controlledEntity = entity;
    }

    /**
     * 将力、力矩、速度、角速度、位置、姿态等数据施加于各个运动体
     */
    public void applyAllEffects() {
        //物理模拟线程将会每计算帧调用一次此方法，因此应在这里写入所有要附加于运动体的力与力矩
        if (rotNeedsUpdate) {
            applySetRotation();
            rotNeedsUpdate = false;
        }
        if (posNeedsUpdate) {
            applySetPosition();
            posNeedsUpdate = false;
        }
        if (aVelNeedsUpdate) {
            applySetAngularVel();
            aVelNeedsUpdate = false;
        }
        if (lVelNeedsUpdate) {
            applySetLinearVel();
            lVelNeedsUpdate = false;
        }
    }

    /**
     * 待物理线程未在处理运动和碰撞时，将实体传送至指定位置
     *
     * @param x
     * @param y
     * @param z
     */
    public void setPositionEnqueue(double x, double y, double z) {
        setPositionEnqueue(new DVector3(x, y, z));
    }

    /**
     * 待物理线程未在处理运动和碰撞时，将实体传送至指定位置
     *
     * @param v 实体的新位置坐标
     */
    public void setPositionEnqueue(DVector3 v) {
        this.posToApply = v;
        posNeedsUpdate = true;
    }

    private void applySetPosition() {
        if (controlledEntity.corePart != null) {
            DVector3 delta = new DVector3(posToApply).sub(controlledEntity.corePart.dbody.getPosition());
            for (AbstractPart part : controlledEntity.corePart) {
                if (part == controlledEntity.corePart) part.dbody.setPosition(posToApply);//将根部件移动到新位置
                else part.dbody.setPosition(part.dbody.getPosition().reAdd(delta));//根据坐标相对变化量，移动每个子孙部件的位置
            }
        }
    }

    /**
     * 待物理线程未在处理运动和碰撞时，设置实体速度
     *
     * @param x
     * @param y
     * @param z
     */
    public void setLinearVelEnqueue(double x, double y, double z) {
        setLinearVelEnqueue(new DVector3(x, y, z));
    }

    /**
     * 待物理线程未在处理运动和碰撞时，设置实体速度
     *
     * @param v 实体的新速度
     */
    public void setLinearVelEnqueue(DVector3 v) {
        this.aVelToApply = v;
        aVelNeedsUpdate = true;
    }

    private void applySetLinearVel() {
        if (controlledEntity.corePart != null) {
            DVector3 delta = new DVector3(lVelToApply).sub(controlledEntity.corePart.dbody.getLinearVel());
            for (AbstractPart part : controlledEntity.corePart) {
                if (part == controlledEntity.corePart) part.dbody.setLinearVel(lVelToApply);//设置根部件速度
                else part.dbody.setLinearVel(part.dbody.getLinearVel().reAdd(delta));//根据坐标相对变化量，改变每个部件的速度
            }
        }
    }

    public void setRotationEnqueue(double pitch, double yaw, double roll) {
        setRotationEnqueue(DQuaternion.fromEulerDegrees(pitch, yaw, roll));
    }

    public void setRotationEnqueue(DQuaternionC q) {
        this.rotToApply = (DQuaternion) q;
        rotNeedsUpdate = true;
    }

    private void applySetRotation() {
        DMatrix3 rotT = controlledEntity.corePart.dbody.getRotation().reTranspose();//根节点旋转矩阵求逆，旋转矩阵的转置即其逆矩阵
        DMatrix3 newRot = new DMatrix3();
        Rotation.dRfromQ(newRot, rotToApply);//新朝向，即新的根节点旋转矩阵
        newRot.eqMul(newRot, rotT);//构造一个调整用旋转矩阵，作用是逆转原本根节点的旋转，再将节点旋转至新的朝向
        for (AbstractPart part : controlledEntity.corePart) {
            if (part == controlledEntity.corePart) part.dbody.setQuaternion(rotToApply);//设置根部件的朝向
            else {//调整子部件的朝向
                DMatrix3 rot = (DMatrix3) part.dbody.getRotation();//获取每个子部件的旋转矩阵
                part.dbody.setRotation(rot.eqMul(newRot, rot));//叠加调整矩阵，旋转至新方向
                //调整子部件相对根部件的位置
                DVector3 pos = new DVector3();
                part.fatherPart.dbody.getRelPointPos(part.attachedSlot.getChildPartAttachPos(), pos);//获取部件连接点的绝对坐标
                //TODO:令部件连接点不强制为部件重心位置
                part.dbody.setPosition(pos);//移动子部件
            }
        }
    }

    /**
     * 待物理线程未在处理运动和碰撞时，设置实体角速度
     *
     * @param x
     * @param y
     * @param z
     */
    public void setAngularVelEnqueue(double x, double y, double z) {
        setAngularVelEnqueue(new DVector3(x, y, z));
    }

    /**
     * 待物理线程未在处理运动和碰撞时，设置实体角速度
     *
     * @param v 实体的新角速度
     */
    public void setAngularVelEnqueue(DVector3 v) {
        this.aVelToApply = v;
        aVelNeedsUpdate = true;
    }

    private void applySetAngularVel() {
        if (controlledEntity.corePart != null) {
            DVector3 delta = new DVector3(aVelToApply).sub(controlledEntity.corePart.dbody.getAngularVel());
            for (AbstractPart part : controlledEntity.corePart) {
                if (part == controlledEntity.corePart) part.dbody.setAngularVel(aVelToApply);//设置根部件速度
                else part.dbody.setAngularVel(part.dbody.getAngularVel().reAdd(delta));//根据坐标相对变化量，改变每个部件的速度
            }
        }
    }
}
