package io.github.tt432.machinemax.common.part;

import io.github.tt432.eyelib.client.render.RenderHelper;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.part.slot.BasicModuleSlot;
import io.github.tt432.machinemax.common.part.slot.AbstractPartSlot;
import io.github.tt432.machinemax.common.phys.PhysThreadController;
import io.github.tt432.machinemax.mixin_interfaces.IMixinLevel;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.math.DVector3C;
import io.github.tt432.machinemax.utils.physics.ode.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractPart implements Iterable<AbstractPart>, IPartPhysParameters {
    //基础属性
    @Getter
    @Setter
    protected BasicEntity attachedEntity;//此部件附着的实体
    public RenderHelper renderHelper;//渲染器，用于部件的渲染，无需操作
    @Getter
    @Setter
    protected double health = 1;//部件生命值

    public partTypes PART_TYPE;//部件类型
    //模块化属性
    public AbstractPart fatherPart;//连接的上级部件
    public AbstractPartSlot attachedSlot;//此部件被安装于的槽位
    public DVector3 attachPoint = new DVector3(0, 0, 0);//本部件重心与父节点连接点的相对位置，即坐标原点与被连接点的相对位置
    protected int PART_SLOT_NUM = 0;//此部件的身体部件及武器装备槽位数
    protected int MOD_SLOT_NUM = 0;//此部件的主被动模块槽位数
    public List<AbstractPartSlot> childrenPartSlots;//连接的子代部件槽
    public List<BasicModuleSlot> moduleSlots;//安装的各类主被动模块槽位

    public enum partTypes {//部件分类，常用于判断该部件是否能够安装到指定槽位
        ARMOR,//装甲板
        CORE,//核心
        CHASSIS,//车架
        HULL,//车体
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
    }

    ;
    /*物理运算相关参数*/
    //流体动力相关系数
    public DVector3C airDragCentre = new DVector3(0, 0, 0);//空气阻力/升力作用点(相对重心位置)
    public DVector3C waterDragCentre = new DVector3(0, 0, 0);//水阻力/升力作用点(相对重心位置)
    //TODO:浮力
    //TODO:摩擦力
    //TODO:不强制每个部件都有匹配的运动体，可令其直接附着于父部件的运动体上，减少运动体与约束的数量，提升稳定性
    public DBody dbody;//部件对应的运动体
    public DMass dmass;//部件对应的质量与转动惯量
    public DGeom[] dgeoms;//部件对应的碰撞体组(可用多个碰撞体拼合出一个部件的碰撞体积)

    public AbstractPart(BasicEntity attachedEntity) {
        this.attachedEntity = attachedEntity;
        dmass = OdeHelper.createMass();
        dbody = OdeHelper.createBody(((IMixinLevel)attachedEntity.level()).machine_Max$getPhysThread().world, this);
    }

    @Override
    public Iterator<AbstractPart> iterator() {
        return new PartIterator();
    }

    class PartIterator implements Iterator<AbstractPart> {
        int index = 0;
        boolean first = true;

        @Override
        public boolean hasNext() {
            if (first) return true;
            if (PART_SLOT_NUM == 0) {
                return false;
            }
            int index0 = index;
            if (index0 < PART_SLOT_NUM) {//按顺序读取槽位，检查槽位状态
                if (AbstractPart.this.childrenPartSlots.get(index0).hasPart()) {
                    return true;
                } else {//若槽位是空槽，跳过此槽位检查下一槽位
                    index++;
                    return hasNext();
                }
            } else {//读取完全部槽位，则肯定无后续
                return false;
            }
        }

        @Override
        public AbstractPart next() {
            if (first) {//首先返回部件本身
                first = false;
                return AbstractPart.this;
            }
            index++;
            return AbstractPart.this.childrenPartSlots.get(index - 1).getChildPart();
        }
    }

    public void addAllGeomsToSpace() {
        for (DGeom geom : dgeoms) {
            ((IMixinLevel)attachedEntity.level()).machine_Max$getPhysThread().space.geomAddEnQueue(geom);
        }
    }

    public void removeAllGeomsInSpace() {
        for (DGeom geom : dgeoms) {
            ((IMixinLevel)attachedEntity.level()).machine_Max$getPhysThread().space.geomRemoveEnQueue(geom);
        }
    }

    public void removeBodyInWorld() {
        dbody.getWorld().bodyRemoveEnQueue(this.dbody);
    }

    /**
     * 获取部件质量(kg)
     *
     * @return 部件质量(kg)
     */
    abstract public double getMass();

    /**
     * 更新部件质量
     */
    abstract public void updateMass();

    /**
     * 获取部件等效护甲(RHA mm)
     *
     * @return 部件等效护甲(RHA mm)
     */
    abstract public double getArmor();

    /**
     * 获取部件生命值上限
     *
     * @return 部件生命值上限
     */
    abstract public double getMaxHealth();

    abstract public ResourceLocation getModel();

    abstract public ResourceLocation getTexture();

    abstract public ResourceLocation getAnimation();

    abstract public ResourceLocation getAniController();
}
