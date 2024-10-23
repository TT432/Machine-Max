package io.github.tt432.machinemax.common.entity.part.slot;

import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;
import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import org.joml.Quaternionf;

public class BasicPartSlot {
    public  AbstractMMPart slotOwnerPart;
    private AbstractMMPart childPart;
    protected DVector3 childPartAttachPos;//子代部件相对本部件质心的连接点位置
    protected DQuaternion childPartAttachRot;//子代部件相对本部件姿态的连接点姿态
    //TODO:连接的关节约束类型和约束参数？
    /**
     * 尝试将给定零件安装到此槽位
     * @param part 要安装的身体部件或武器装备
     */
    public void attachPart(AbstractMMPart part){
        if(this.childPart == null && slotConditionCheck(part)) {
            this.childPart =part;
            part.father_part=this.slotOwnerPart;
            //TODO:对齐连接点，连接关节约束
        }
    }

    /**
     * 将此槽位连接的部件与本部件断开连接
     */
    public void detachPart(){
        if(this.childPart != null) {
            //TODO:断开关节约束
            this.childPart.father_part=null;
            this.childPart = null;
        }
    }

    /**
     * 检查给定零部件是否符合本槽位的安装要求
     * @param part 要检查的待安装部件
     * @return 给定零部件是否满足当前槽位安装条件
     */
    public boolean slotConditionCheck(AbstractMMPart part){
        return true;//如有为此槽位指定安装条件的需要，继承此类并重载此方法
    }

    public DVector3 getChildPartAttachPos() {
        return childPartAttachPos;
    }

    public void setChildPartAttachPos(DVector3 childPartAttachPos) {
        this.childPartAttachPos = childPartAttachPos;
    }

    public DQuaternion getChildPartAttachRot() {
        return childPartAttachRot;
    }

    public void setChildPartAttachRot(DQuaternion childPartAttachRot) {
        this.childPartAttachRot = childPartAttachRot;
    }
}
