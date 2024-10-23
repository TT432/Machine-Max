package io.github.tt432.machinemax.utils.formula;

import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.DBody;

/**
 * 此类中集中收纳了本模组与动力学有关的机理公式，方便管理与调用
 * @author 甜粽子
 */
public class MMDynamic {
    /**
     * 根据给定部件的运动状态计算其受到的气动力
     * @param part 要计算受力的零部件
     * @return 相对部件自身坐标的部件受力向量
     */
    public static DVector3 AerodynamicForce(AbstractMMPart part){
        DVector3 force = new DVector3(0,0,0);
        return  force;
    }
    /**
     * 根据给定部件的运动状态计算其受到的水动力
     * @param part 要计算受力的零部件
     * @return 相对部件自身坐标的部件受力向量
     */
    public static DVector3 HydrodynamicForce(AbstractMMPart part){
        DVector3 force = new DVector3(0,0,0);
        return  force;
    }
}
