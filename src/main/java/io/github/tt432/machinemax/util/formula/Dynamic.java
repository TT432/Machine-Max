package io.github.tt432.machinemax.util.formula;

import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.util.physics.math.DVector3;

/**
 * 此类中集中收纳了本模组与动力学有关的机理公式，方便管理与调用
 * @author 甜粽子
 */
public class Dynamic {
    /**
     * 根据给定部件的运动状态计算其受到的气动力
     * @param part 要计算受力的零部件
     * @return 相对部件自身坐标的部件受力向量
     */
    public static DVector3 AerodynamicForce(AbstractPart part){
        DVector3 force = new DVector3(0,0,0);
        return  force;
    }
    /**
     * 根据给定部件的运动状态计算其受到的水动力
     * @param part 要计算受力的零部件
     * @return 相对部件自身坐标的部件受力向量
     */
    public static DVector3 HydrodynamicForce(AbstractPart part){
        DVector3 force = new DVector3(0,0,0);
        return  force;
    }
}
