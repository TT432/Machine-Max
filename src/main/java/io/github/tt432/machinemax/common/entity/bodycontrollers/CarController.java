package io.github.tt432.machinemax.common.entity.bodycontrollers;

import io.github.tt432.machinemax.common.entity.BasicPhysController;
import io.github.tt432.machinemax.common.entity.MMMBasicEntity;
import io.github.tt432.machinemax.common.entity.entities.TestCarEntity;

public class CarController extends BasicPhysController {

    public CarController(MMMBasicEntity entity) {
        super(entity);
    }

    @Override
    public void applyForceAndTorques() {
        //((TestCarEntity)controlledEntity).dbody.addRelForce(0,0,500);
        super.applyForceAndTorques();
    }
}
