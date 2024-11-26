package io.github.tt432.machinemax.common.part.slot;

import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.common.phys.PhysThread;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.*;

public class WheelPartSlot extends AbstractPartSlot {

    public double kp;//悬挂刚度系数，单位N/m
    public double kd;//悬挂阻尼系数，单位N/(m/s)

    public WheelPartSlot(AbstractPart owner, String locator, double kp, double kd) {
        super(owner, locator);
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
        ((DHinge2Joint) joints.getFirst()).setAxes(0, 1, 0, 1, 0, 0);
        ((DHinge2Joint) joints.getFirst()).setParamLoStop(-0);//限制轮胎转角
        ((DHinge2Joint) joints.getFirst()).setParamHiStop(0);
        //设置减震器属性
        ((DHinge2Joint) joints.getFirst()).setParamSuspensionERP(((double) PhysThread.STEP / 1000 * kp) / (((double) PhysThread.STEP / 1000 * kp) + kd));
        ((DHinge2Joint) joints.getFirst()).setParamSuspensionCFM(1 / (((double) PhysThread.STEP / 1000 * kp) + kd));
    }

    @Override
    public boolean slotConditionCheck(AbstractPart part) {
        return part.PART_TYPE == AbstractPart.partTypes.WHEEL;
    }
}
