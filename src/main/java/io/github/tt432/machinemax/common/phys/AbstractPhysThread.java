package io.github.tt432.machinemax.common.phys;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.util.data.BodiesSyncData;
import io.github.tt432.machinemax.util.physics.math.DVector3;
import io.github.tt432.machinemax.util.physics.ode.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static io.github.tt432.machinemax.util.physics.ode.OdeConstants.*;
import static io.github.tt432.machinemax.util.physics.ode.OdeHelper.areConnectedExcluding;

abstract public class AbstractPhysThread extends Thread {
    public final Level level;//物理计算线程与每个维度绑定，即每个维度都有一个物理计算线程
    public final DWorld world;//容纳所有碰撞体与运动体的世界
    public Map<Integer, BodiesSyncData> syncData = HashMap.newHashMap(100);//用于同步的线程内所有运动体位姿速度数据
    public volatile DSpace space;
    public volatile DJointGroup contactGroup;
    public volatile boolean isPaused = false;
    public static final long STEP_SIZE = 20;//物理线程计算步长(毫秒)
    protected volatile int step = 0;//物理运算迭代运行的总次数

    AbstractPhysThread(Level level) {
        this.level = level;
        OdeHelper.initODE();//初始化物理库
        world = OdeHelper.createWorld();
        init();
    }

    abstract public void run();

    /**
     * 初始化物理模拟线程，设置仿真基本参数
     */
    protected void init() {
        isPaused = false;
        MachineMax.LOGGER.info("New phys thread started!");
        world.setGravity(0, -9.81, 0);//设置重力
        world.setContactSurfaceLayer(0.01);//最大陷入深度，有助于防止抖振(虽然本来似乎也没)
        world.setERP(0.25);
        world.setCFM(0.00005);
        world.setAutoDisableFlag(true);//设置静止物体自动休眠以节约性能
        world.setAutoDisableSteps(5);
        world.setQuickStepNumIterations(40);//设定迭代次数以提高物理计算精度
        world.setQuickStepW(1.3);
        world.setContactMaxCorrectingVel(20);
        //TODO:区分碰撞空间(常规)，命中判定空间(弹头刀刃等放进来)和自体碰撞空间(头发布料等有物理没碰撞的放进来)
        space = OdeHelper.createHashSpace();//碰撞空间，用于容纳各类碰撞体
        contactGroup = OdeHelper.createJointGroup();
        OdeHelper.createPlane(space, 0, 1, 0, -60);//创造碰撞平面
        step = 0;
    }

    /**
     * 在物理仿真未处于暂停状态时推进主仿真进程
     *
     * @param paused -是否暂停了物理仿真进程
     */
    protected void regularStep(boolean paused) {
        internalStep(paused);
        step++;
        space.handleGeomAddAndRemove();//增删碰撞体
        world.handleBodyRemove();//删除待删除的运动体
    }

    /**
     * 在物理仿真未处于暂停状态时推进仿真进程，不进行运动体和碰撞体的增删处理
     *
     * @param paused -是否暂停了物理仿真进程
     */
    protected void internalStep(boolean paused) {
        if (!paused) {
            applyAllControllers(space);
            space.collide(null, nearCallback);//碰撞检测
            world.quickStep((double) STEP_SIZE / 1000);
            contactGroup.empty();//碰撞处理完成后移除所有碰撞点约束
        }
    }

    protected DGeom.DNearCallback nearCallback = this::nearCallback;

    protected void nearCallback(Object data, DGeom o1, DGeom o2) {
        //MachineMax.LOGGER.info("NearCallback called!");
        //exit without doing anything if the two bodies are connected by a joint
        DBody b1 = o1.getBody();
        DBody b2 = o2.getBody();
        if (b1 != null && b2 != null && areConnectedExcluding(b1, b2, DJoint.class)) {
            return;
        }
        BasicEntity e1;
        BasicEntity e2;
        if (b1 != null && b2 != null) {
            e1 = b1.getAttachedPart().getAttachedEntity();
            e2 = b2.getAttachedPart().getAttachedEntity();
            if (e1 == e2) {
                return;
            }
        }
        int contactNum = 64;
        DContactBuffer contacts = new DContactBuffer(contactNum);   // up to MAX_CONTACTS contacts per box-box
        for (int i = 0; i < contactNum; i++) {
            DContact contact = contacts.get(i);
            if (b1 != null && b1.getAttachedPart().PART_TYPE == AbstractPart.partTypes.WHEEL) {
                contact.surface.mode = dContactBounce | dContactRolling | dContactApprox1_N | dContactFDir1;
                contact.surface.mu = 2000;
                contact.surface.rho = 0.01;
                contact.surface.bounce = 0.0001;
                contact.surface.bounce_vel = 0.1;
                DVector3 vf = new DVector3();
                b1.vectorToWorld(new DVector3(-1, 0, 0), vf);
                vf.cross(contact.geom.normal);
                contact.fdir1.set(vf);
            } else if (b2 != null && b2.getAttachedPart().PART_TYPE == AbstractPart.partTypes.WHEEL) {
                contact.surface.mode = dContactBounce | dContactRolling | dContactApprox1_N | dContactFDir1;
                contact.surface.mu = 2000;
                contact.surface.rho = 0.01;
                contact.surface.bounce = 0.0001;
                contact.surface.bounce_vel = 0.1;
                DVector3 vf = new DVector3();
                b2.vectorToWorld(new DVector3(-1, 0, 0), vf);
                vf.cross(contact.geom.normal);
                contact.fdir1.set(vf);
            } else {
                contact.surface.mode = dContactBounce | dContactRolling;
                contact.surface.mu = 5000;
                contact.surface.rho = 0.01;
                contact.surface.bounce = 0.0001;
                contact.surface.bounce_vel = 0.1;
            }
        }
        int numc = OdeHelper.collide(o1, o2, contactNum, contacts.getGeomBuffer());
        if (numc != 0) {
            for (int i = 0; i < numc; i++) {
                DJoint c = OdeHelper.createContactJoint(world, contactGroup, contacts.get(i));
                c.attach(b1, b2);
            }
        }
    }

    /**
     * 调用所有实体物理控制器，根据控制律使力与力矩作用于指定碰撞空间的运动体
     *
     * @param space 生效的碰撞空间
     */
    protected void applyAllControllers(@NotNull DSpace space) {
        if (space.getNumGeoms() > 0) {
            //应用控制器前，先重置其状态
            for (DGeom g : space.getGeoms()) {//获取所有碰撞体
                if (g.getBody() != null) {
                    BasicEntity e = g.getBody().getAttachedPart().getAttachedEntity();//获取碰撞体所附着的运动体所附着的实体
                    if (e.isControllerHandled()) {//若附着实体的控制器在上次循环中已生效
                        e.setControllerHandled(false);//重置控制器状态
                    }
                }
            }
            //应用控制器各类效果
            for (DGeom g : space.getGeoms()) {//获取所有碰撞体
                if (g.getBody() != null) {
                    BasicEntity e = g.getBody().getAttachedPart().getAttachedEntity();//获取碰撞体所附着的运动体所附着的实体
                    if (!e.isControllerHandled() && e.getController() != null) {//若附着实体的控制器在本次循环中未生效
                        e.getController().applyAllEffects();//根据控制律，为每个运动体施加相应的效果
                        e.setControllerHandled(true);//标记此实体的控制器已处理
                    }
                }
            }
        }
    }
}
