package io.github.tt432.machinemax.common.phys;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.utils.physics.ode.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.openjdk.jmh.annotations.Benchmark;

import static io.github.tt432.machinemax.utils.physics.ode.OdeConstants.*;
import static io.github.tt432.machinemax.utils.physics.ode.OdeHelper.areConnectedExcluding;

public class PhysThread extends Thread {
    public static volatile DWorld world;
    public static volatile DSpace renderSpace;
    public static volatile DSpace serverSpace;
    public static volatile DJointGroup contactGroup;
    public static volatile boolean isPaused = false;
    static final long step = 10;//物理线程计算步长(毫秒)
    public static volatile long time = 0;
    @Override
    public void run() {//物理计算的主线程
        isPaused = false;
        OdeHelper.initODE2(0);//初始化物理库
        MachineMax.LOGGER.info("New phys thread started!");
        world = OdeHelper.createWorld();//各个物体所处的世界，不处于同一世界的物体无法交互，或许可以用来做不同维度的处理，但是否会无法利用多线程优势？
        world.setGravity(0, -9.81, 0);//设置重力
        world.setContactSurfaceLayer(0.001);//最大陷入深度，有助于防止抖振(虽然本来似乎也没)
        world.setERP(0.1);
        world.setCFM(0.00005);
        //TODO:设置静止物体自动休眠以节约性能
        world.setQuickStepNumIterations(40);//设定迭代次数以提高物理计算精度
        world.setQuickStepW(1.3);
        world.setContactMaxCorrectingVel(20);
        renderSpace = OdeHelper.createHashSpace();//碰撞空间，用于容纳各类碰撞体（大概），负责客户端物体的碰撞
        serverSpace = OdeHelper.createHashSpace();//碰撞空间，用于容纳各类碰撞体（大概），负责服务端物体的碰撞
        contactGroup = OdeHelper.createJointGroup();
        OdeHelper.createPlane(renderSpace, 0, 1, 0, -60);//创造碰撞平面
        OdeHelper.createPlane(serverSpace, 0, 1, 0, -60);//创造碰撞平面
        while (!isInterrupted()) {//物理线程主循环
            long startTime = System.nanoTime();//记录开始时间
            step(isPaused);//推进物理模拟计算进程
            long duration = (System.nanoTime() - startTime) / 1000000;//计算物理线程执行用时，并转换为毫秒
            long sleepTime = step - duration;
            if (sleepTime < 1) sleepTime = 1;
            //MachineMax.LOGGER.info("Execute time:"+duration);
            try {
                Thread.sleep(sleepTime);//等待
            } catch (InterruptedException e) {
                MachineMax.LOGGER.info("Stopping phys thread...");
                contactGroup.destroy();
                renderSpace.destroy();
                serverSpace.destroy();
                world.destroy();
                break;
            }
        }
    }

    /**
     * 在物理仿真未处于暂停状态时推进仿真进程
     *
     * @param paused -是否暂停了物理仿真进程
     */
    public void step(boolean paused) {
        if (!paused) {
            applyAllControllers(renderSpace);
            applyAllControllers(serverSpace);
            renderSpace.collide(null, nearCallback);//碰撞检测
            serverSpace.collide(null, nearCallback);//碰撞检测
            world.quickStep((double) step / 1000);
        }
        contactGroup.empty();//碰撞处理完成后移除所有碰撞点约束
        renderSpace.handleGeomAddAndRemove();//增删待增删的碰撞体
        serverSpace.handleGeomAddAndRemove();
        world.handleBodyRemove();//删除待删除的运动体
        time=System.nanoTime();
    }

    private DGeom.DNearCallback nearCallback = new DGeom.DNearCallback() {
        @Override
        public void call(Object data, DGeom o1, DGeom o2) {
            //MachineMax.LOGGER.info("Calling nearCallback...");
            nearCallback(data, o1, o2);
        }
    };

    private void nearCallback(Object data, DGeom o1, DGeom o2) {
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
            contact.surface.mode = dContactBounce | dContactRolling | dContactApprox1;
            contact.surface.mu = 5000;
            contact.surface.rho = 0.01;
            contact.surface.bounce = 0.0001;
            contact.surface.bounce_vel = 0.1;
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
    private void applyAllControllers(@NotNull DSpace space) {
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
