package io.github.tt432.machinemax.common.entity.physcontrollers;

import io.github.tt432.machinemax.common.entity.BasicPhysController;
import io.github.tt432.machinemax.common.entity.MMBasicEntity;

public class CarController extends BasicPhysController {

    public CarController(MMBasicEntity entity) {
        super(entity);
    }

    @Override
    public void applyForceAndTorques() {
        //((TestCarEntity)controlledEntity).dbody.addRelForce(0,0,500);
        super.applyForceAndTorques();
    }
}
