package io.github.tt432.machinemax.common.part.slot;

import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.common.phys.PhysThread;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.DAMotorJoint;
import io.github.tt432.machinemax.utils.physics.ode.DHinge2Joint;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;

public class SteeringWheelPartSlot extends AbstractPartSlot {

    public double kp;//悬挂刚度系数，单位N/m
    public double kd;//悬挂阻尼系数，单位N/(m/s)

    public SteeringWheelPartSlot(AbstractPart owner, double kp, double kd) {
        super(owner);
        this.kp = kp;
        this.kd = kd;
    }

    @Override
    protected void attachJoint(AbstractPart part) {
        joints.add(OdeHelper.createHinge2Joint(part.dbody.getWorld()));
        joints.getFirst().attach(slotOwnerPart.dbody, part.dbody);
        DVector3 pos = new DVector3();
        slotOwnerPart.dbody.getRelPointPos(childPartAttachPos, pos);
        ((DHinge2Joint) joints.getFirst()).setAnchor(pos);
        ((DHinge2Joint) joints.getFirst()).setAxes(0, -1, 0, -1, 0, 0);
        ((DHinge2Joint) joints.getFirst()).setParamLoStop(-0*Math.PI/4);//限制轮胎转角
        ((DHinge2Joint) joints.getFirst()).setParamHiStop(0*Math.PI/4);
//        ((DHinge2Joint) joints.getFirst()).setParamLoStop(-0);//限制轮胎转角
//        ((DHinge2Joint) joints.getFirst()).setParamHiStop(0);
        //设置转向驱动
        joints.add(OdeHelper.createAMotorJoint(part.dbody.getWorld()));
        joints.get(1).attach(slotOwnerPart.dbody, part.dbody);
        ((DAMotorJoint) joints.get(1)).setMode(DAMotorJoint.AMotorMode.dAMotorEuler);
        ((DAMotorJoint) joints.get(1)).setAxis(0,1,0,1,0);
        ((DAMotorJoint) joints.get(1)).setAxis(1,2,1,0,0);
        ((DAMotorJoint) joints.get(1)).setParamLoStop2(-Math.PI/4);//限制轮胎转角
        ((DAMotorJoint) joints.get(1)).setParamHiStop2(Math.PI/4);
        //设置减震器属性
        ((DHinge2Joint) joints.getFirst()).setParamSuspensionERP(((double) PhysThread.step /1000*kp)/(((double) PhysThread.step /1000*kp)+kd));
        ((DHinge2Joint) joints.getFirst()).setParamSuspensionCFM(1/(((double) PhysThread.step /1000*kp)+kd));
    }

    @Override
    public boolean slotConditionCheck(AbstractPart part) {
        return part.PART_TYPE == AbstractPart.partTypes.WHEEL;
    }
}
