package io.github.tt432.machinemax.util.formula;
/**
 * 此类中集中收纳了本模组与伤害有关的机理公式，方便管理与调用
 * @author 甜粽子
*/
public class Damage {
    /**
     * 计算实体投射物对护盾的伤害
     * @param pDamage 投射物伤害
     * @param pMultiplier 护盾伤害倍率
     * @param pPenetrationIndex 护盾穿透系数
     * @return 对护盾造成的实际伤害
     */
    public static double BulletDamage2Shield(float pDamage, float pMultiplier, float pPenetrationIndex){
        return pDamage*pMultiplier*(1F-pPenetrationIndex);
    }
    /**
     * 计算实体投射物对生命值的伤害
     * @param pDamage 投射物伤害
     * @return 对生命值造成的实际伤害
     */
    public static double BulletDamage2Health(float pDamage){
        return pDamage;
    }
}
