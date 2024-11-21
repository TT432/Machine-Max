package io.github.tt432.machinemax.common.part.slot;

import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.utils.physics.ode.DFixedJoint;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;

public class HullPartSlot extends AbstractPartSlot {
    public HullPartSlot(AbstractPart owner, String locator) {
        super(owner,locator);
    }

    @Override
    protected void attachJoint(AbstractPart part) {
        joints.add(OdeHelper.createFixedJoint(part.dbody.getWorld()));
        joints.getFirst().attach(part.dbody, this.slotOwnerPart.dbody);
        ((DFixedJoint)joints.getFirst()).setFixed();
    }

    public boolean slotConditionCheck(AbstractPart part) {
        return part.PART_TYPE == AbstractPart.partTypes.HULL;
    }
}
