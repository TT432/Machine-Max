package io.github.tt432.machinemax.common.entity;

import net.minecraft.world.entity.Entity;

/**
 * 此类为实体的物理控制器原型
 * 这里应写有所有模组实体的物理运动逻辑(表面摩擦力除外，物理引擎自行处理了)
 * @author 甜粽子
 */
public class BasicPhysController {

    protected MMBasicEntity controlledEntity;//此控制器控制的实体

    public BasicPhysController(MMBasicEntity entity){
        this.controlledEntity=entity;
    }
    /**
     * 将力与力矩施加于各个物理体
     */
    public void applyForceAndTorques(){
        //物理模拟线程将会每计算帧调用一次此方法，因此应在这里写入所有要附加于物理体的力与力矩
    }

    public Entity getControlledEntity() {
        return this.controlledEntity;
    }

    public void setControlledEntity(MMBasicEntity controlledEntity) {
        this.controlledEntity = controlledEntity;
    }
}
