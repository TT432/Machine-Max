package io.github.tt432.machinemax.common.entity.controller;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.part.slot.AbstractPartSlot;
import io.github.tt432.machinemax.common.phys.PhysThread;
import io.github.tt432.machinemax.utils.MMMath;
import io.github.tt432.machinemax.utils.control.PDController;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.DAMotorJoint;
import io.github.tt432.machinemax.utils.physics.ode.DBody;
import io.github.tt432.machinemax.utils.physics.ode.DHinge2Joint;

import static java.lang.Math.*;

public class CarController extends PhysController {
    //TODO:把这些参数挪到其他地方
    public double MAX_POWER = 20000;//最大总功率30kW
    public double MAX_FORWARD_RPM;//最大前进发动机转速，自动计算，取决于最大前进速度
    public double MAX_BACKWARD_RPM;//最大倒车发动机转速，自动计算，取决于最大倒车速度
    public double IDLE_RPM;//发动机待机转速，决定起步加减速能力
    public double MAX_BRAKE_POWER = 5000;//最大单轮刹车力矩5000Nm
    public double ENG_ACC = 0.05D;//引擎加速系数
    public double ENG_DEC = 0.15D;//引擎减速系数
    public double STEER_T = 0.25D;//达到满舵所需时间
    public double MIN_TURNING_R = 5;//最小转弯半径

    public double power = 0D;//推进功率
    public double brake = 0D;
    public double turning_input = 0D;

    public CarController(BasicEntity entity) {
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
        DHinge2Joint joint;
        joint = (DHinge2Joint) controlledEntity.corePart.childrenPartSlots.get(0).joints.getFirst();
        joint.addTorques(0, -power / 2 / (abs(joint.getAngle1Rate()) + 2 * Math.PI));//电机驱动力
        joint = (DHinge2Joint) controlledEntity.corePart.childrenPartSlots.get(1).joints.getFirst();
        joint.addTorques(0, -power / 2 / (abs(joint.getAngle1Rate()) + 2 * Math.PI));//电机驱动力
        for (int i = 0; i < 4; i++) {
            joint = (DHinge2Joint) controlledEntity.corePart.childrenPartSlots.get(i).joints.getFirst();
            joint.addTorques(0, brake * MMMath.sigmoidSignum(abs(joint.getAngle1Rate())));//刹车制动力
        }
    }

    private void steer() {//转向轮控制
        rudderControl();
        DHinge2Joint hinge;
        DAMotorJoint motor;
        for (int i = 0; i < 2; i++) {
            hinge = (DHinge2Joint) controlledEntity.corePart.childrenPartSlots.get(0).joints.getFirst();
            motor = (DAMotorJoint) controlledEntity.corePart.childrenPartSlots.get(i).joints.get(1);
            controlledEntity.corePart.childrenPartSlots.get(i).getChildPart().dbody.setFiniteRotationAxis(0, 0, 0);
            //TODO:由转弯半径分别计算各个轮胎的最大转角
            double m2 = -turning_input * PI / 4 - hinge.getAngle1();
            motor.setParamVel(m2);
        }
    }

    private void engineControl() {//发动机输出计算
        //目标输出功率
        double target_power = MAX_POWER * rawMoveInput[2] / 100;
        double target_brake;
        DVector3 v = controlledEntity.corePart.dbody.getLinearVel().copy();
        controlledEntity.corePart.dbody.vectorFromWorld(v, v);
        if (v.get2() >= -0.1 && rawMoveInput[2] > 0) {//未在后退时按w则前进
            target_brake = 0;
        } else if (v.get2() > 0 && rawMoveInput[2] < 0) {//前进时按s则刹车
            target_brake = MAX_BRAKE_POWER * rawMoveInput[2] / 100;
        } else if (v.get2() <= 0.1 && rawMoveInput[2] < 0) {//未在前进时按s则后退
            target_brake = 0;
        } else if (v.get2() < 0 && rawMoveInput[2] > 0) {//后退时按w则刹车
            target_brake = MAX_BRAKE_POWER * rawMoveInput[2] / 100;
        } else if ((abs(v.get2()) <= 0.05 && rawMoveInput[2] == 0) || moveInputConflict[2] == 1) {//无输入且低速，或输入冲突时刹车
            target_brake = MAX_BRAKE_POWER * rawMoveInput[2] / 100;
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
        if (rawMoveInput[4] > 0 || (rawMoveInput[4] == 0 && turning_input < -PhysThread.STEP)) {
            turning_input = clamp(turning_input + PhysThread.STEP / STEER_T, -1, 1);
        } else if (rawMoveInput[4] < 0 || turning_input > PhysThread.STEP) {
            turning_input = clamp(turning_input - PhysThread.STEP / STEER_T, -1, 1);
        } else {
            turning_input = 0F;
        }
    }
}
