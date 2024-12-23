package io.github.tt432.machinemax.common.phys;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.util.data.BodiesSyncData;
import org.ode4j.ode.DBody;
import org.ode4j.ode.internal.DxBody;
import net.minecraft.world.level.Level;

import java.util.Iterator;

public class LocalPhysThread extends AbstractPhysThread {

    public volatile boolean needSync = false;
    LocalPhysThread(Level level) {
        super(level);
    }

    @Override
    public void run() {//物理计算的主线程
        while (!isInterrupted()) {//物理线程主循环
            long startTime = System.nanoTime();//记录开始时间
            //TODO:一个内部无sleep的while循环，用于根据服务器数据从数据包时间处开始快速迭代直到追上本地时间以修正误差
            regularStep(isPaused);//推进物理模拟计算进程
            updateMolang();//更新用于渲染模型姿态位置的molang变量
            long duration = (System.nanoTime() - startTime) / 1000000;//计算物理线程执行用时，并转换为毫秒
            long sleepTime = STEP_SIZE - duration;
            if (sleepTime < 1) sleepTime = 1;
            try {
                Thread.sleep(sleepTime);//等待
            } catch (InterruptedException e) {
                MachineMax.LOGGER.info("Stopping local phys thread...");
                contactGroup.destroy();
                space.destroy();
                world.destroy();
                break;
            }
        }
    }

    @Override
    protected void regularStep(boolean paused) {
        if (needSync) {
            syncBodies();
            needSync = false;
        }
        super.regularStep(paused);
    }
    /**
     * 更新本维度内每个运动体的位置、姿态、速度和角速度
     */
    protected void syncBodies() {
//        for (Iterator<DBody> it = world.getBodyIterator(); it.hasNext(); ) {//遍历线程内所有运动体
//            DxBody b = (DxBody) it.next();
//            if (syncData.get(b.getId()) != null) {//若同步数据包内包含对应运动体的信息
//                BodiesSyncData data = syncData.get(b.getId());
//                b.setPosition(data.pos());//同步位置
//                b.setQuaternion(data.rot());//同步姿态
//                b.setLinearVel(data.lVel());//同步线速度
//                b.setAngularVel(data.aVel());//同步角速度
//            }
//        }
    }
    protected void updateMolang(){
//        for (Iterator<DBody> it = world.getBodyIterator(); it.hasNext(); ) {//遍历线程内所有运动体
//            DxBody b = (DxBody) it.next();
//            AbstractPart part = b.getAttachedPart();
//            if (part!= null && part.molangScope!= null) {
//                part.molangScope.updatePhysMolang();
//            }
//        }
    }
}
