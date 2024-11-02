package io.github.tt432.machinemax.common.entity.part.slot;

import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;
import io.github.tt432.machinemax.utils.physics.ode.DFixedJoint;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;

public class HullPartSlot extends AbstractPartSlot {
    public HullPartSlot(AbstractMMPart owner) {
        super(owner);
    }

    @Override
    protected void attachJoint(AbstractMMPart part) {
        joints.add(OdeHelper.createFixedJoint(part.dbody.getWorld()));
        joints.getFirst().attach(part.dbody, this.slotOwnerPart.dbody);
        ((DFixedJoint)joints.getFirst()).setFixed();
    }

    public boolean slotConditionCheck(AbstractMMPart part) {
        return part.PART_TYPE == AbstractMMPart.partTypes.HULL;
    }
}
