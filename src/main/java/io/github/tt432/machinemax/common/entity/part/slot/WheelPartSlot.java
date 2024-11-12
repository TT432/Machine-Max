package io.github.tt432.machinemax.common.entity.part.slot;

import io.github.tt432.machinemax.common.entity.part.AbstractPart;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.*;

public class WheelPartSlot extends AbstractPartSlot {
    public WheelPartSlot(AbstractPart owner) {
        super(owner);
    }

    @Override
    protected void attachJoint(AbstractPart part) {
        joints.add(OdeHelper.createHinge2Joint(part.dbody.getWorld()));
        joints.getFirst().attach(slotOwnerPart.dbody, part.dbody);
        DVector3 pos = new DVector3();
        slotOwnerPart.dbody.getRelPointPos(childPartAttachPos, pos);
        ((DHinge2Joint)joints.getFirst()).setAnchor(pos);
        ((DHinge2Joint)joints.getFirst()).setAxes(0,-1,0,-1,0,0);
        ((DHinge2Joint)joints.getFirst()).setParamLoStop(-0);//限制轮胎转角
        ((DHinge2Joint)joints.getFirst()).setParamHiStop(0);
    }

    @Override
    public boolean slotConditionCheck(AbstractPart part) {
        return part.PART_TYPE == AbstractPart.partTypes.WHEEL;
    }
}
