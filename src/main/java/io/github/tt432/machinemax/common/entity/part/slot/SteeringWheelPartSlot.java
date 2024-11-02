package io.github.tt432.machinemax.common.entity.part.slot;

import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.DHinge2Joint;
import io.github.tt432.machinemax.utils.physics.ode.DPRJoint;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;

public class SteeringWheelPartSlot extends AbstractPartSlot {
    public SteeringWheelPartSlot(AbstractMMPart owner) {
        super(owner);
    }

    @Override
    protected void attachJoint(AbstractMMPart part) {
        joints.add(OdeHelper.createHinge2Joint(part.dbody.getWorld()));
        joints.getFirst().attach(slotOwnerPart.dbody, part.dbody);
        DVector3 pos = new DVector3();
        slotOwnerPart.dbody.getRelPointPos(childPartAttachPos, pos);
        ((DHinge2Joint)joints.getFirst()).setAnchor(pos);
        ((DHinge2Joint)joints.getFirst()).setAxes(0,-1,0,-1,0,0);
        ((DHinge2Joint)joints.getFirst()).setParamLoStop(-Math.PI/4);//限制轮胎转角
        ((DHinge2Joint)joints.getFirst()).setParamHiStop(Math.PI/4);
    }

    @Override
    public boolean slotConditionCheck(AbstractMMPart part) {
        return part.PART_TYPE == AbstractMMPart.partTypes.WHEEL;
    }
}
