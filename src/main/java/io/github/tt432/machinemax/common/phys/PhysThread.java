package io.github.tt432.machinemax.common.phys;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.MMMBasicEntity;
import io.github.tt432.machinemax.utils.ode.*;

import static io.github.tt432.machinemax.utils.ode.OdeConstants.*;
import static io.github.tt432.machinemax.utils.ode.OdeHelper.areConnectedExcluding;

public class PhysThread extends Thread{
    public static volatile DWorld world;
    public static volatile DSpace renderSpace;
    public static volatile DSpace serverSpace;
    public static volatile DJointGroup contactGroup;
    public static volatile boolean isPaused;
    @Override
    public void run() {//此乃物理计算的主线程
        isPaused =false;
        OdeHelper.initODE2(0);//初始化物理库
        MachineMax.LOGGER.info("New phys thread started!");
        world = OdeHelper.createWorld();//各个物体所处的世界，不处于同一世界的物体无法交互，或许可以用来做不同维度的处理，但是否会无法利用多线程优势？
        world.setGravity(0,-9.81,0);//设置重力
        world.setERP(0.1);
        world.setContactSurfaceLayer(0.001);//最大陷入深度，有助于防止抖振(虽然本来似乎也没)
        renderSpace = OdeHelper.createHashSpace();//碰撞空间，用于容纳各类碰撞体（大概），负责客户端物体的碰撞
        serverSpace = OdeHelper.createHashSpace();//碰撞空间，用于容纳各类碰撞体（大概），负责服务端物体的碰撞
        contactGroup = OdeHelper.createJointGroup();
        OdeHelper.createPlane(renderSpace,0,1,0,-60);//创造碰撞平面
        OdeHelper.createPlane(serverSpace,0,1,0,-60);//创造碰撞平面
        while(!isInterrupted()){//物理线程主循环
            step(isPaused);//推进物理模拟计算进程
            try {
                //TODO:根据前文执行用时调整sleep时间？能做到吗？
                Thread.sleep(10);//等待时间步长
            } catch (InterruptedException e) {
                MachineMax.LOGGER.info("Stopping phys thread...");
                break;
            }
        }
        renderSpace.destroy();
        serverSpace.destroy();
        world.destroy();
    }

    /**
     * 在物理仿真未处于暂停状态时推进仿真进程
     * @param paused -是否暂停了物理仿真进程
     */
    public void step(boolean paused){
        if(!paused){
            applyAllControllers(renderSpace);
            applyAllControllers(serverSpace);
            renderSpace.collide(null,nearCallback);//碰撞检测
            serverSpace.collide(null,nearCallback);//碰撞检测
            world.quickStep(0.01);
        }
        contactGroup.empty();//碰撞处理完成后移除所有碰撞点约束
        renderSpace.handleGeomAddAndRemove();
        serverSpace.handleGeomAddAndRemove();
    }

    private DGeom.DNearCallback nearCallback = new DGeom.DNearCallback() {
        @Override
        public void call(Object data, DGeom o1, DGeom o2){
            //MachineMax.LOGGER.info("Calling nearCallback...");
            nearCallback(data, o1, o2);
        }
    };

    private void nearCallback(Object data, DGeom o1, DGeom o2){
        //MachineMax.LOGGER.info("NearCallback called!");
        int i;
        // exit without doing anything if the two bodies are connected by a joint
        DBody b1 = o1.getBody();
        DBody b2 = o2.getBody();
        if (b1!=null && b2!=null && areConnectedExcluding (b1,b2,DContactJoint.class)) {
            return;
        }

        DContactBuffer contacts = new DContactBuffer(128);   // up to MAX_CONTACTS contacts per box-box
        for (i=0; i<128; i++) {
            DContact contact = contacts.get(i);
            contact.surface.mode = dContactBounce|dContactRolling|dContactApprox1;
            contact.surface.mu = 50000;
            contact.surface.rho = 0.01;
            contact.surface.bounce = 0.001;
            contact.surface.bounce_vel = 0.1;
        }
        int numc = OdeHelper.collide (o1,o2,128,contacts.getGeomBuffer());
        if (numc!=0) {
            for (i=0; i<numc; i++) {
                DJoint c = OdeHelper.createContactJoint (world,contactGroup,contacts.get(i));
                c.attach (b1,b2);
            }
        }
    }

    /**
     * 调用所有实体物理控制器，根据控制律使力与力矩作用于指定碰撞空间的物理体
     * @param space 生效的碰撞空间
     */
    private void applyAllControllers(DSpace space){
        if (space.getNumGeoms()>0){
            //应用控制器前，先重置其状态
            for(DGeom g : space.getGeoms()){//获取所有碰撞体
                if(g.getBody()!=null){
                    MMMBasicEntity e = g.getBody().getAttachedEntity();//获取碰撞体所附着的物理体所附着的实体
                    if(e.isControllerHandled()){//若附着实体的控制器在上次循环中已生效
                        e.setControllerHandled(false);//重置控制器状态
                    }
                }
            }
            for(DGeom g : space.getGeoms()){//获取所有碰撞体
                if(g.getBody()!=null){
                    MMMBasicEntity e = g.getBody().getAttachedEntity();//获取碰撞体所附着的物理体所附着的实体
                    if(!e.isControllerHandled() && e.getController() != null){//若附着实体的控制器在本次循环中未生效
                        e.getController().applyForceAndTorques();//根据控制律，为每个物理体施加相应的力与力矩
                        e.setControllerHandled(true);//标记此实体的控制器已处理
                    }
                }
            }
        }
    }
}
