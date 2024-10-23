package io.github.tt432.machinemax.common.entity.part;

import io.github.tt432.machinemax.utils.physics.ode.DBody;
import io.github.tt432.machinemax.utils.physics.ode.DGeom;
import io.github.tt432.machinemax.utils.physics.ode.DMass;

public class BasicPart {

    public DBody dbody;
    public DMass dmass;
    public DGeom dgeom;

    private double BASIC_MASS;
    private double BASIC_HEALTH;
    private double BASIC_ARMOR;
    enum PART_TYPE{
        BASIC,
        CORE,
        WHEEL,
        TRACK,
        WEAPON,
        ARM,
        LEG,
        HEAD,
        BACK,
        TURRET
    };
}
