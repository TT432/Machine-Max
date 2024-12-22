package io.github.tt432.machinemax.common.entity.controller;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.part.slot.AbstractPartSlot;
import io.github.tt432.machinemax.common.part.slot.WheelPartSlot;
import io.github.tt432.machinemax.common.phys.AbstractPhysThread;
import io.github.tt432.machinemax.util.MMMath;
import io.github.tt432.machinemax.util.physics.math.DVector3;
import io.github.tt432.machinemax.util.physics.ode.DAMotorJoint;
import io.github.tt432.machinemax.util.physics.ode.DHinge2Joint;

import static java.lang.Math.*;

public class AE86Controller extends PhysController {
    public double MAX_POWER = 100000;//最大总功率100kW
    public double MAX_FORWARD_RPM;//最大前进发动机转速，自动计算，取决于最大前进速度
    public double MAX_BACKWARD_RPM;//最大倒车发动机转速，自动计算，取决于最大倒车速度
    public double IDLE_RPM;//发动机待机转速，决定起步加减速能力
    public double MAX_BRAKE_POWER = 600;//最大单轮刹车力矩500Nm
    public double ENG_ACC = 0.02D;//引擎加速系数
    public double ENG_DEC = 0.05D;//引擎减速系数
    public double STEER_T = 0.25D;//达到满舵所需时间
    public double MIN_TURNING_R = 7;//最小转弯半径

    public double power = 0D;//推进功率
    public double brake = 0D;
    public double turning_input = 0D;

    public AE86Controller(BasicEntity entity) {
        super(entity);
    }

    @Override
    public void applyAllEffects() {
        drive();
        steer();
        for (int i = 0; i < 4; i++)
            controlledEntity.corePart.childrenPartSlots.get(i).getChildPart().dbody.setFiniteRotationAxis(0, 0, 0);
        super.applyAllEffects();
    }

    private void drive() {//驱动轮胎
        engineControl();
        //驱动力
        AbstractPartSlot slot;
        DHinge2Joint joint;
        DVector3 omega;
        double torque;
        for (int i = 1; i < 4; i++){
            slot = controlledEntity.corePart.childrenPartSlots.get(i);
            joint = (DHinge2Joint) slot.joints.getFirst();
            omega = joint.getBody(1).getAngularVel().copy();//获取轮胎角速度
            joint.getBody(1).vectorFromWorld(omega, omega);//转换为自体坐标
            torque = -power / 4 / (abs(omega.get0()) + Math.PI/12);
            joint.addTorques(0, torque);//电机驱动力
        }
        //刹车力
        for (int i = 0; i < 4; i++) {
            slot = controlledEntity.corePart.childrenPartSlots.get(i);
            joint = (DHinge2Joint) slot.joints.getFirst();
            omega = joint.getBody(1).getAngularVel().copy();//获取轮胎角速度
            joint.getBody(1).vectorFromWorld(omega, omega);//转换为自体坐标
            torque = -brake * MMMath.sigmoidSignum(omega.get0()*signum(slot.getChildPartAttachPos().get0()));
            joint.addTorques(0, torque);//刹车制动力矩
        }
    }

    private void steer() {//转向轮控制
        rudderControl();
        DHinge2Joint hinge;
        DAMotorJoint motor;
        AbstractPartSlot slot;
        //TODO:排查高速下转向困难的原因
        for (int i = 0; i < 4; i++) {
            slot = controlledEntity.corePart.childrenPartSlots.get(i);
            hinge = (DHinge2Joint) slot.joints.getFirst();
            motor = (DAMotorJoint) slot.joints.get(1);
            slot.getChildPart().dbody.setFiniteRotationAxis(0, 0, 0);
            double m2;
            if(turning_input == 0||slot instanceof WheelPartSlot){//无转向输入，或不具备转向功能则维持前向
                m2 = - hinge.getAngle1();
            }else {//由转弯半径分别计算各个轮胎的最大转角(阿克曼转向)
                double lr_half;
                if(i==0||i==3) lr_half = (double) 28 /16;
                else lr_half = (double) -28 /16;
                lr_half*=signum(-turning_input);
                double lwb = (double) (34 + 40) /16;
                m2 = atan(lwb/(MIN_TURNING_R/-turning_input+lr_half)) - hinge.getAngle1();
            }
            motor.setParamVel(m2);
        }
    }

    private void engineControl() {//发动机输出计算
        //目标输出功率
        double target_power = MAX_POWER * rawMoveInput[2] / 100;
        double target_brake;
        DVector3 vAbs = controlledEntity.corePart.dbody.getLinearVel().copy();
        DVector3 v = new DVector3();
        controlledEntity.corePart.dbody.vectorFromWorld(vAbs, v);
        if (v.get2() >= -0.5 && rawMoveInput[2] > 0 && moveInputConflict[2] == 0) {//未在后退时按w则前进
            target_brake = 0;
        } else if (v.get2() > 0 && rawMoveInput[2] < 0 && moveInputConflict[2] == 0) {//前进时按s则刹车
            target_brake = MAX_BRAKE_POWER * abs(rawMoveInput[2]) / 100;
        } else if (v.get2() <= 0.5 && rawMoveInput[2] < 0 && moveInputConflict[2] == 0) {//未在前进时按s则后退
            target_brake = 0;
        } else if (v.get2() < 0 && rawMoveInput[2] > 0 && moveInputConflict[2] == 0) {//后退时按w则刹车
            target_brake = MAX_BRAKE_POWER * abs(rawMoveInput[2]) / 100;
        } else if ((abs(v.get2()) <= 0.5 && rawMoveInput[2] == 0) || moveInputConflict[2] == 1) {//无输入且低速，或输入冲突时刹车
            target_brake = MAX_BRAKE_POWER;
        } else {//溜车
            target_brake = 0;
        }
        if ((target_power >= 0 && target_power >= power) || (target_power <= 0 && target_power <= power)) {//增加引擎输出功率
            power = (1 - ENG_ACC) * power + ENG_ACC * target_power;
        } else {//降低引擎输出功率
            power = (1 - ENG_DEC) * power + ENG_DEC * target_power;
        }
        if ((target_brake >= 0 && target_brake >= brake) || (target_brake <= 0 && target_brake <= brake)) {//增加刹车力
            brake = (1 - 0.2) * brake + 0.2 * target_brake;
        } else {//降低刹车力
            brake = (1 - 0.5) * brake + 0.5 * target_brake;
        }
    }

    private void rudderControl() {//转角计算
        if (rawMoveInput[4] > 0 || (rawMoveInput[4] == 0 && turning_input < -AbstractPhysThread.STEP_SIZE)) {
            turning_input = clamp(turning_input + AbstractPhysThread.STEP_SIZE / STEER_T, -1, 1);
        } else if (rawMoveInput[4] < 0 || turning_input > AbstractPhysThread.STEP_SIZE) {
            turning_input = clamp(turning_input - AbstractPhysThread.STEP_SIZE / STEER_T, -1, 1);
        } else {
            turning_input = 0F;
        }
    }
}
