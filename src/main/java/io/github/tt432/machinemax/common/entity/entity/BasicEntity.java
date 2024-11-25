package io.github.tt432.machinemax.common.entity.entity;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.controller.PhysController;
import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class BasicEntity extends LivingEntity implements IMMEntityAttribute {

    @Setter
    @Getter
    private PhysController controller = new PhysController(this);//实体指定的控制器，默认为基础控制器
    @Setter
    @Getter
    private controlMode mode = BasicEntity.controlMode.GROUND;//采用的控制模式，决定接收的按键输入方案
    public AbstractPart corePart;//实体连接的核心部件
    @Setter
    @Getter
    private volatile boolean controllerHandled;//控制器是否已在单帧物理计算中生效
    private int physPosSyncTick;
    private int physRotSyncTick;
    private DVector3 physSyncDeltaPos;
    private DVector3 physSyncDeltaSpdL;
    private DQuaternion physSyncDeltaRot;
    private DVector3 physSyncDeltaSpdA;
    private DVector3 posError = new DVector3();
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;
    @Setter
    @Getter
    private float ZRot;

    public enum controlMode {
        GROUND,
        SHIP,
        PLANE,
        MECH
    }

    public BasicEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        noPhysics = true;
        physPosSyncTick = 0;
    }

    @Override
    public void tick() {
        if (firstTick) {//完成实体初始化后将运动体位姿与实体同步，并加入物理计算线程中
            if (corePart != null) {
                setPos(this.getX(), this.getY(), this.getZ());//将所有部件的位置同步到物理计算线程
                setRot(this.getXRot(), this.getYRot(), this.getZRot());//将所有部件的姿态同步到物理计算线程
                for (AbstractPart part : this.corePart) part.addAllGeomsToSpace();//将所有部件添加进碰撞空间
            }
        }
        this.syncPoseToMainThread();//将实体位姿与物理计算结果同步
        if (!this.level().isClientSide()) {//服务端限定内容

            //MachineMax.LOGGER.info("enabled?: " + corePart.dbody.isEnabled());
        } else {//客户端限定内容
//            MachineMax.LOGGER.info("rot: " + corePart.dbody.getQuaternion().copy().toEulerDegreesZXY());
        }
        super.tick();
    }

    /***
     * 将物理计算线程中的实体位姿同步到游戏主线程
     */
    public void syncPoseToMainThread() {
        if (corePart != null) {//将实体位姿与物理计算结果同步
            DVector3 pos = corePart.dbody.getPosition().copy();
            this.setPosRaw(pos.get0(), pos.get1(), pos.get2());
            DQuaternion dq = corePart.dbody.getQuaternion().copy();
            DVector3 heading = dq.toEulerDegreesZXY();
            setXRot((float) heading.get0());
            setYRot((float) heading.get1());
            setZRot((float) heading.get2());
            this.setBoundingBox(this.makeBoundingBox());
        }
    }

    /**
     * 将主线程和物理线程中的实体及其子部件传送到新的位置
     *
     * @param x x坐标(m)
     * @param y y坐标(m)
     * @param z z坐标(m)
     */
    @Override
    public void setPos(double x, double y, double z) {
        setPos(new DVector3(x, y, z));
        super.setPos(x, y, z);
    }

    public void setPos(DVector3 v) {
        if (this.getController() != null) controller.setPositionEnqueue(v);
    }

    /**
     * 设置主线程和物理线程中实体及其子部件的姿态
     *
     * @param pitch 俯仰角(deg)
     * @param yaw   偏航角(deg)
     * @param roll  滚转角(deg)
     */
    public void setRot(double pitch, double yaw, double roll) {
        setRot(DQuaternion.fromEulerDegrees(pitch, yaw, roll));
    }

    public void setRot(DQuaternion q) {
        if (this.getController() != null) controller.setRotationEnqueue(q);
        DVector3 ang = q.toEulerDegreesZXY();
        setXRot((float) ang.get0());
        setYRot((float) ang.get1());
        setZRot((float) ang.get2());
    }

    /**
     * 根据各个零部件的位置与质量计算实体的质心位置
     *
     * @return 实体的质心位置
     */
    public DVector3 getMassCentre() {
        double totalMass = this.corePart.dmass.getMass();//获取根部件质量
        DVector3 massCentre = this.corePart.dbody.getPosition().reScale(totalMass);//获取根部件质心位置，并以质量为权重
        for (AbstractPart part : this.corePart) {
            if (part != this.corePart) {
                massCentre.add(part.dbody.getPosition().reScale(part.dmass.getMass()));
                totalMass += part.dmass.getMass();//计算总重
            }
        }
        massCentre.scale(1 / totalMass);//加权平均求质心位置
        return massCentre;
    }

    @Override
    public void move(@NotNull MoverType type, @NotNull Vec3 pos) {
        //运动交由物理引擎处理，原版运动留空
    }

    @Override
    public boolean isPickable() {//不是是否可拾取而是是否可被选中
        return !this.isRemoved();
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        this.remove(RemovalReason.KILLED);
        return true;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public void remove(RemovalReason reason) {
        if (this.corePart != null) {
            for (AbstractPart part : this.corePart) {
                part.dbody.enable();
                part.removeAllGeomsInSpace();
                part.removeBodyInWorld();
            }
        }
        super.remove(reason);
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
        return this.lerpSteps > 0 ? (float) this.lerpXRot : this.getXRot();
    }

    @Override
    public float lerpTargetYRot() {
        return this.lerpSteps > 0 ? (float) this.lerpYRot : this.getYRot();
    }

}
