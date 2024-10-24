package io.github.tt432.machinemax.common.entity.entity;

import io.github.tt432.machinemax.common.entity.physcontroller.BasicPhysController;
import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BasicEntity extends Entity {

    public BasicPhysController CONTROLLER;//实体指定的控制器
    public AbstractMMPart CORE_PART;//实体连接的核心部件
    public boolean controllerHandled;//控制器是否已在单帧物理计算中生效
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;
    private float ZRot;

    public BasicEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        tickLerp();
        super.tick();
    }

    @Override
    public boolean isPickable() {//不是是否可拾取而是是否可被选中
        return !this.isRemoved();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        this.kill();
        return true;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return canVehicleCollide(this, pEntity);
    }

    public static boolean canVehicleCollide(Entity pVehicle, Entity pEntity) {
        return (pEntity.canBeCollidedWith() || pEntity.isPushable()) && !pVehicle.isPassengerOfSameVehicle(pEntity);
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }
    }
    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYRot, float pXRot, int pSteps) {
        this.lerpX = pX;
        this.lerpY = pY;
        this.lerpZ = pZ;
        this.lerpYRot = pYRot;
        this.lerpXRot = pXRot;
        this.lerpSteps = 10;
    }

    @Override
    public double lerpTargetX() {
        return this.lerpSteps > 0 ? this.lerpX : this.getX();
    }

    @Override
    public double lerpTargetY() {
        return this.lerpSteps > 0 ? this.lerpY : this.getY();
    }

    @Override
    public double lerpTargetZ() {
        return this.lerpSteps > 0 ? this.lerpZ : this.getZ();
    }

    @Override
    public float lerpTargetXRot() {
        return this.lerpSteps > 0 ? (float)this.lerpXRot : this.getXRot();
    }

    @Override
    public float lerpTargetYRot() {
        return this.lerpSteps > 0 ? (float)this.lerpYRot : this.getYRot();
    }

    public boolean isControllerHandled() {
        return controllerHandled;
    }

    public void setControllerHandled(boolean controllerHandled) {
        this.controllerHandled = controllerHandled;
    }

    public BasicPhysController getController() {
        return this.CONTROLLER;
    }

    public void setController(BasicPhysController controller) {
        this.CONTROLLER = controller;
    }

    public float getZRot() {
        return ZRot;
    }

    public void setZRot(float ZRot) {
        this.ZRot = ZRot;
    }
}
