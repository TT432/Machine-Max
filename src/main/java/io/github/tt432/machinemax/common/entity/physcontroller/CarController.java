package io.github.tt432.machinemax.common.entity.physcontroller;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.entity.part.slot.AbstractPartSlot;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.DHinge2Joint;
import io.github.tt432.machinemax.utils.physics.ode.DHingeJoint;
import io.github.tt432.machinemax.utils.physics.ode.DJoint;
import io.github.tt432.machinemax.utils.physics.ode.DPRJoint;

public class CarController extends BasicPhysController {

    public CarController(BasicEntity entity) {
        super(entity);
    }

    @Override
    public void applyAllEffects() {
        AbstractPartSlot slot;
//        slot =controlledEntity.CORE_PART.children_parts.get(0);
//        ((DHinge2Joint)slot.joints.getFirst()).addTorques(0,150);
//        slot =controlledEntity.CORE_PART.children_parts.get(1);
//        ((DHingeJoint)slot.joints.getFirst()).addTorque(150);
//        slot =controlledEntity.CORE_PART.children_parts.get(2);
//        ((DHingeJoint)slot.joints.getFirst()).addTorque(150);
//        slot =controlledEntity.CORE_PART.children_parts.get(3);
//        ((DHinge2Joint)slot.joints.getFirst()).addTorques(0,150);
        super.applyAllEffects();
    }
}
