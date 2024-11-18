package io.github.tt432.machinemax.common.entity.controller;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.part.slot.AbstractPartSlot;

public class CarController extends PhysController {

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
