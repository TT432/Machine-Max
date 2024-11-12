package io.github.tt432.machinemax.common.entity.part.slot;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.part.AbstractPart;
import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.DJoint;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 部件槽位原型
 */
abstract public class AbstractPartSlot {
    public AbstractPart slotOwnerPart;
    @Getter
    private AbstractPart childPart;//槽位保存的子部件
    @Setter
    @Getter
    protected DVector3 childPartAttachPos;//子代部件相对本部件质心的连接点位置
    @Setter
    @Getter
    protected DQuaternion childPartAttachRot;//子代部件相对本部件姿态的连接点姿态

    public List<DJoint> joints = new ArrayList<>();//存储槽位包含的关节约束，以便访问

    public AbstractPartSlot(AbstractPart owner){
        this.slotOwnerPart = owner;
    }
    /**
     * 尝试将给定零件安装到此槽位
     * @param part 要安装的身体部件或武器装备
     */
    public void attachPart(AbstractPart part){
        if(hasPart()) {
            MachineMax.LOGGER.error("Failed to attach the part, because the slot already has a part!");
        } else if (!slotConditionCheck(part)) {
            MachineMax.LOGGER.error("Failed to attach the part, because the part doesn't match the slot's condition!");
        } else {
            this.childPart = part;
            part.father_part = this.slotOwnerPart;
            part.attachedSlot = this;
            DVector3 pos = new DVector3();//临时变量
            this.slotOwnerPart.dbody.vectorToWorld(this.childPartAttachPos,pos);//获取连接点在世界坐标系下的位置
            part.dbody.setPosition(pos);//子部件重心对齐连接点
            part.dbody.setQuaternion(this.childPartAttachRot);//调整姿态
            //TODO:令部件连接点不强制为部件重心位置
            attachJoint(part);
        }
    }

    abstract protected void attachJoint(AbstractPart part);

    /**
     * 将此槽位连接的部件与本部件断开连接
     */
    public void detachPart(){
        if(hasPart()) {
            this.childPart.father_part=null;
            this.childPart = null;
            detachJoint();
        }
    }

    protected void detachJoint() {
        for(DJoint joint : this.joints){
            joint.destroy();
        }
    }

    /**
     * 检查给定零部件是否符合本槽位的安装要求
     * @param part 要检查的待安装部件
     * @return 给定零部件是否满足当前槽位安装条件
     */
    public boolean slotConditionCheck(AbstractPart part){
        return true;//如有为此槽位指定安装条件的需要，继承此类并重载此方法
    }

    /**
     * 检查该槽位是否安装了部件
     * @return 检查结果
     */
    public boolean hasPart(){
        return this.childPart != null;
    }

}
