package io.github.tt432.machinemax.common.entity.part;

import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.entity.part.slot.BasicModuleSlot;
import io.github.tt432.machinemax.common.entity.part.slot.BasicPartSlot;
import io.github.tt432.machinemax.common.phys.PhysThread;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.math.DVector3C;
import io.github.tt432.machinemax.utils.physics.ode.DBody;
import io.github.tt432.machinemax.utils.physics.ode.DGeom;
import io.github.tt432.machinemax.utils.physics.ode.DMass;
import io.github.tt432.machinemax.utils.physics.ode.OdeHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMMPart {

    private BasicEntity attachedEntity;//此部件附着的实体

    static double BASIC_MASS;//(kg)
    static double BASIC_HEALTH;
    static double BASIC_ARMOR;//(RHA mm)
    public static partTypes PART_TYPE;

    int PART_SLOT_NUM;//此部件的身体部件及武器装备槽位数
    int MOD_SLOT_NUM;//此部件的主被动模块槽位数
    public List<BasicPartSlot> children_parts;//连接的子代部件
    public AbstractMMPart father_part;//连接的上级部件
    public DVector3 attach_point;//本部件重心与父节点连接点的相对位置，即坐标原点与被连接点的相对位置
    public List<BasicModuleSlot> modules;//安装的各类主被动模块

    private double health;//部件生命值
    private double armor;//部件护甲

    public enum partTypes{//部件分类，常用于判断该部件是否能够安装到指定槽位
        ARMOR,//装甲板
        CORE,//核心
        WHEEL,//轮胎
        TRACK,//履带
        WEAPON,//武器
        GEAR,//其他装备
        LEFT_ARM,//左臂
        RIGHT_ARM,//右臂
        LEFT_LEG,//左腿
        RIGHT_LEG,//右腿
        HEAD,//头部
        BACKPACK,//背包
        TURRET//炮塔(不包含武器)
    };

    //以下为物理运算相关参数，控制部件的运动受力
    final DVector3C MASS_CENTRE=new DVector3(0,0,0);//物理引擎要求重心位于运动体原点处
    //气动力相关系数
    static double BASIC_AIRDRAG_COEF_ZP;//空气阻力系数(前向)，一般较小
    static double BASIC_AIRDRAG_COEF_ZN;//空气阻力系数(后向)，一般较小
    static double BASIC_AIRDRAG_COEF_XP;//空气阻力系数(左向)
    static double BASIC_AIRDRAG_COEF_XN;//空气阻力系数(右向)
    static double BASIC_AIRDRAG_COEF_YP;//空气阻力系数(上向)
    static double BASIC_AIRDRAG_COEF_YN;//空气阻力系数(下向)
    static double BASIC_AIRLIFT_COEF_Z;//空气升力系数(前向)，形状带来的额外升力
    static double BASIC_AIRLIFT_COEF_X;//空气升力系数(水平向)，一般为0
    static double BASIC_AIRLIFT_COEF_Y;//空气升力系数(垂向)，一般为0
    static DVector3C AIRDRAG_CENTRE;//空气阻力/升力作用点(相对重心位置)
    //水动力相关系数
    static double BASIC_WATERDRAG_COEF_ZP;//水阻力系数(前向)
    static double BASIC_WATERDRAG_COEF_ZN;//水阻力系数(后向)
    static double BASIC_WATERDRAG_COEF_XP;//水阻力系数(左向)
    static double BASIC_WATERDRAG_COEF_XN;//水阻力系数(右向)
    static double BASIC_WATERDRAG_COEF_YP;//水阻力系数(上向)
    static double BASIC_WATERDRAG_COEF_YN;//水阻力系数(下向)
    static double BASIC_WATERLIFT_COEF_Z;//水升力系数(前向)
    static double BASIC_WATERLIFT_COEF_X;//水升力系数(水平向)
    static double BASIC_WATERLIFT_COEF_Y;//水升力系数(垂向)
    static DVector3C WATERDRAG_CENTRE;//水阻力/升力作用点(相对重心位置)
    //TODO:浮力
    //TODO:摩擦力
    public DBody dbody;//部件对应的运动体
    public DMass dmass;//部件对应的质量与转动惯量
    public DGeom[] dgeoms;//部件对应的碰撞体组(可用多个碰撞体拼合出一个部件的碰撞体积)

    public AbstractMMPart(BasicEntity attachedEntity){
        this.attachedEntity = attachedEntity;
        dmass = OdeHelper.createMass();
        dbody = OdeHelper.createBody(PhysThread.world,this);
    }

//    public void depthFirstTraversal(AbstractMMPart part){
//        if (!(this.children_parts ==null) && !this.children_parts.isEmpty()) {
//            for(AbstractMMPart child : children_parts){
//                depthFirstTraversal(child);//遍历子节点
//            }
//        }
//    }

    /**
     * 根据运动体的相对运动状态，计算总的气动力系数
     * @return 气动力系数
     */
    public DVector3 getAerodynamicForceCoef() {
        DVector3 coef=new DVector3(BASIC_AIRLIFT_COEF_X,BASIC_AIRLIFT_COEF_Y,BASIC_AIRLIFT_COEF_Z);
        DVector3 vAbs = new DVector3();
        dbody.getRelPointVel(AIRDRAG_CENTRE, vAbs);//获取升力作用点的绝对速度
        DVector3 vRel = new DVector3();
        dbody.vectorFromWorld(vAbs, vRel);//绝对速度转换为相对速度
        if(vRel.get0()>0){
            coef.add0(BASIC_AIRDRAG_COEF_XP);
        } else {
            coef.add0(BASIC_AIRDRAG_COEF_XN);
        }
        if(vRel.get1()>0){
            coef.add1(BASIC_AIRDRAG_COEF_YP);
        } else {
            coef.add1(BASIC_AIRDRAG_COEF_YN);
        }
        if(vRel.get2()>0){
            coef.add2(BASIC_AIRDRAG_COEF_ZP);
        } else {
            coef.add2(BASIC_AIRDRAG_COEF_ZN);
        }
        return coef;
    }

    /**
     * 根据运动体的相对运动状态，计算总的水动力系数
     * @return 水动力系数
     */
    public DVector3 getHydrodynamicForceCoef() {
        DVector3 coef=new DVector3(BASIC_WATERLIFT_COEF_X,BASIC_WATERLIFT_COEF_Y,BASIC_WATERLIFT_COEF_Z);
        DVector3 vAbs = new DVector3();
        dbody.getRelPointVel(WATERDRAG_CENTRE, vAbs);//获取升力作用点的绝对速度
        DVector3 vRel = new DVector3();
        dbody.vectorFromWorld(vAbs, vRel);//绝对速度转换为相对速度
        if(vRel.get0()>0){
            coef.add0(BASIC_WATERDRAG_COEF_XP);
        } else {
            coef.add0(BASIC_WATERDRAG_COEF_XN);
        }
        if(vRel.get1()>0){
            coef.add1(BASIC_WATERDRAG_COEF_YP);
        } else {
            coef.add1(BASIC_WATERDRAG_COEF_YN);
        }
        if(vRel.get2()>0){
            coef.add2(BASIC_WATERDRAG_COEF_ZP);
        } else {
            coef.add2(BASIC_WATERDRAG_COEF_ZN);
        }
        return coef;
    }

    public BasicEntity getAttachedEntity() {
        return attachedEntity;
    }

    public void setAttachedEntity(BasicEntity attachedEntity) {
        this.attachedEntity = attachedEntity;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getArmor() {
        return armor;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public double getMaxHealth(){
        return BASIC_HEALTH;
    }

    public double getMaxArmor(){
        return BASIC_ARMOR;
    }

    public double getMass(){
        return BASIC_MASS;
    }

}
