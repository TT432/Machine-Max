package io.github.tt432.machinemax.common.entity.physcontroller;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;

public class CarController extends BasicPhysController {

    public CarController(BasicEntity entity) {
        super(entity);
    }

    @Override
    public void applyForceAndTorques() {
        //((TestCarEntity)controlledEntity).dbody.addRelForce(0,0,500);
        super.applyForceAndTorques();
    }
}
