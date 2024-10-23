package io.github.tt432.machinemax.common.entity.part;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;

public abstract class AbstractWheelPart extends AbstractMMPart {

    public double WHEEL_RADIUS;//轮半径
    public double WHEEL_WIDTH;//轮宽度

    public AbstractWheelPart(BasicEntity entity) {
        super(entity);
        PART_TYPE = partTypes.WHEEL;
    }
}
