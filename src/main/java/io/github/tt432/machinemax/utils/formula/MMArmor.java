package io.github.tt432.machinemax.utils.formula;

import net.minecraft.world.level.block.Block;

import static java.lang.Math.cos;
import static java.lang.Math.log10;

/**
 * 此类中集中收纳了本模组与护甲有关的机理公式，方便管理与调用
 * @author 甜粽子
 */
public class MMArmor {
    /**
     * 考虑入射角的影响，计算不同入射角下的等效护甲水平
     * @param pArmor 基础护甲水平
     * @param pAngle 入射角(投射物速度方向与装甲法线方向的夹角)
     * @return 等效护甲水平
     */
    public static double equivalentArmor(float pArmor, float pAngle){
        return pArmor/cos(pAngle);
    }
    /**
     * 获取方块护甲水平，护甲公式为：200*lg(1+爆炸抗性)*(方块硬度)
     * @param pBlock 要计算护甲水平的方块
     * @return 给定方块的护甲水平
     */
    public static double blockArmor(Block pBlock){
        return 200D*(log10(pBlock.getExplosionResistance()+1)*pBlock.defaultDestroyTime());
    }
}
