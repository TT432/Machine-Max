package io.github.tt432.machinemax.util.formula;

import io.github.tt432.machinemax.common.part.AbstractPart;
import org.ode4j.math.DVector3;

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
    public static DVector3 aerodynamicForce(AbstractPart part){
        DVector3 F = new DVector3(-0.5, -0.5, -0.5);
        DVector3 cf = part.getAerodynamicForceCoef(part);
        DVector3 v = new DVector3();
        part.dbody.getRelPointVel(part.airDragCentre, v);//获取绝对速度
        part.dbody.vectorFromWorld(v.copy(), v);//转为自体坐标系
        v.scale(v.copy().eqAbs());
        F.scale(v);
        F.scale(cf);
        return F;
    }
    /**
     * 根据给定部件的运动状态计算其受到的水动力
     * @param part 要计算受力的零部件
     * @return 相对部件自身坐标的部件受力向量
     */
    public static DVector3 hydrodynamicForce(AbstractPart part){
        DVector3 F = new DVector3(-0.5, -0.5, -0.5);
        DVector3 cf = part.getHydrodynamicForceCoef(part);
        DVector3 v = new DVector3();
        part.dbody.getRelPointVel(part.airDragCentre, v);//获取绝对速度
        part.dbody.vectorFromWorld(v.copy(), v);//转为自体坐标系
        v.scale(v.copy().eqAbs());
        F.scale(v);
        F.scale(cf);
        return F;
    }
}
