package io.github.tt432.machinemax.common.part.slot;

import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.util.physics.math.DQuaternion;
import io.github.tt432.machinemax.util.physics.math.DVector3;
import io.github.tt432.machinemax.util.physics.ode.DFixedJoint;
import io.github.tt432.machinemax.util.physics.ode.OdeHelper;

public class HullPartSlot extends AbstractPartSlot {
    public HullPartSlot(AbstractPart owner, String locator, DVector3 attachPos, DQuaternion attachRot) {
        super(owner,locator, attachPos, attachRot);
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
