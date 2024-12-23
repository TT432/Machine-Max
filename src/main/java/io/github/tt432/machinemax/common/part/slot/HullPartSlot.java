package io.github.tt432.machinemax.common.part.slot;

import io.github.tt432.machinemax.common.part.AbstractPart;
import org.ode4j.math.DQuaternion;
import org.ode4j.math.DVector3;
import org.ode4j.ode.DFixedJoint;
import org.ode4j.ode.OdeHelper;

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
