package io.github.tt432.machinemax.common.part;

import io.github.tt432.machinemax.common.entity.entity.PartEntity;

public abstract class AbstractWheelPart extends AbstractPart {

    public double WHEEL_RADIUS;//轮半径
    public double WHEEL_WIDTH;//轮宽度
    public double brakeTorque = 0;

    public AbstractWheelPart(PartEntity entity) {
        super(entity);
        dbody.setFiniteRotationMode(true);
//        dbody.setGyroscopicMode(false);
        PART_TYPE = partTypes.WHEEL;
    }
}
