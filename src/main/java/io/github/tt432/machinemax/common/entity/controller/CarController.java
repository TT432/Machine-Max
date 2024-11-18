package io.github.tt432.machinemax.common.entity.controller;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.part.slot.AbstractPartSlot;
import io.github.tt432.machinemax.utils.physics.ode.DHinge2Joint;

public class CarController extends PhysController {

    public CarController(BasicEntity entity) {
        super(entity);
    }

    @Override
    public void applyAllEffects() {
        AbstractPartSlot slot;
        slot =controlledEntity.corePart.children_parts.get(0);
        ((DHinge2Joint)slot.joints.getFirst()).addTorques(0,rawMoveInput[2]);
        slot =controlledEntity.corePart.children_parts.get(1);
        ((DHinge2Joint)slot.joints.getFirst()).addTorques(0,rawMoveInput[2]);
        slot =controlledEntity.corePart.children_parts.get(2);
        ((DHinge2Joint)slot.joints.getFirst()).addTorques(0,rawMoveInput[2]);
        slot =controlledEntity.corePart.children_parts.get(3);
        ((DHinge2Joint)slot.joints.getFirst()).addTorques(0,rawMoveInput[2]);
        super.applyAllEffects();
    }
}
