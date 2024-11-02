package io.github.tt432.machinemax.common.entity.part.slot;

import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.*;

public class WheelPartSlot extends AbstractPartSlot {
    public WheelPartSlot(AbstractMMPart owner) {
        super(owner);
    }

    @Override
    protected void attachJoint(AbstractMMPart part) {
        joints.add(OdeHelper.createHingeJoint(part.dbody.getWorld()));
        joints.getFirst().attach(slotOwnerPart.dbody, part.dbody);
        DVector3 pos = new DVector3();
        slotOwnerPart.dbody.getRelPointPos(childPartAttachPos, pos);
        ((DHingeJoint)joints.getFirst()).setAnchor(pos);
        ((DHingeJoint)joints.getFirst()).setAxis(-1,0,0);
    }

    @Override
    public boolean slotConditionCheck(AbstractMMPart part) {
        return part.PART_TYPE == AbstractMMPart.partTypes.WHEEL;
    }
}
