package io.github.tt432.machinemax.common.entity.entity;

import io.github.tt432.machinemax.common.entity.physcontroller.BasicPhysController;
import io.github.tt432.machinemax.common.entity.part.AbstractMMPart;
import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.github.tt432.machinemax.utils.physics.ode.DGeom;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;

public class BasicEntity extends LivingEntity implements MMEntityAttributeC{

    private BasicPhysController CONTROLLER = new BasicPhysController(this);//实体指定的控制器，默认为基础控制器
    public AbstractMMPart CORE_PART;//实体连接的核心部件
    public boolean controllerHandled;//控制器是否已在单帧物理计算中生效
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;
    private float ZRot;

    public BasicEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        noPhysics=true;
    }

    //注册其他需要同步的信息
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }
    //同步信息发生更新时的处理
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
    }

    @Override
    public void tick() {
        if(firstTick){//完成实体初始化后将运动体位姿与实体同步，并加入物理计算线程中
            if(CORE_PART!=null){

                //TODO:旋转处理
                DQuaternion dq = DQuaternion.fromEulerDegrees(this.getXRot(),this.getYRot(),this.getZRot());
                this.CORE_PART.dbody.setQuaternion(dq);
                for (AbstractMMPart part : this.CORE_PART) {
                    part.dbody.setQuaternion(dq);//再将所有连接着的子部件添加进碰撞空间
                }
                setPos(this.getX(),this.getY(),this.getZ());//将所有部件的位置同步到物理计算线程
                CORE_PART.addAllGeomsToSpace();//将核心部件加入碰撞空间
                for (AbstractMMPart part : this.CORE_PART) {
                    part.addAllGeomsToSpace();//再将所有连接着的子部件添加进碰撞空间
                }
            }
        }
        this.syncPos();//将实体位姿与物理计算结果同步
        //TODO:将服务器上的实体位置定期同步至客户端
        super.tick();
        tickLerp();
    }

    /***
     * 将物理计算线程中的实体位姿同步到游戏主线程
     */
    public void syncPos(){
        if(CORE_PART!=null){//将实体位姿与物理计算结果同步
            this.setPosRaw(CORE_PART.dbody.getPosition().get0(),CORE_PART.dbody.getPosition().get1(),CORE_PART.dbody.getPosition().get2());
            DQuaternion dq = (DQuaternion) CORE_PART.dbody.getQuaternion();
            DVector3 heading = dq.toEulerDegrees();
            setXRot((float) heading.get0());
            setYRot((float) heading.get1());
            setZRot((float) heading.get2());
        }
        this.setBoundingBox(this.makeBoundingBox());
    }

    /**
     * 将主线程和物理线程中的实体及其子部件传送到新的位置
     * @param x x坐标，
     * @param y y坐标，垂直向上为正
     * @param z z坐标，
     */
    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
        if (this.getController()!=null){
            CONTROLLER.setPositionEnqueue(x,y,z);
        }
    }

    /**
     * 根据各个零部件的位置与质量计算实体的质心位置
     * @return 实体的质心位置
     */
    public DVector3 getMassCentre(){
        double totalMass = this.CORE_PART.dmass.getMass();//获取根部件质量
        DVector3 massCentre = this.CORE_PART.dbody.getPosition().reScale(totalMass);//获取根部件质心位置，并以质量为权重
        for (AbstractMMPart part : this.CORE_PART) {
            massCentre.add(part.dbody.getPosition().reScale(part.dmass.getMass()));
            totalMass += part.dmass.getMass();//计算总重
        }
        massCentre.scale(1/totalMass);//加权平均求质心位置
        return massCentre;
    }

    @Override
    public void move(MoverType type, Vec3 pos) {
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
        for (AbstractMMPart part : this.CORE_PART) {
            part.removeAllGeomsInSpace();
            part.removeBodyInWorld();
        }
        CORE_PART.removeAllGeomsInSpace();
        CORE_PART.removeBodyInWorld();
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
