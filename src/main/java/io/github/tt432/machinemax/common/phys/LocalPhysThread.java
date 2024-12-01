package io.github.tt432.machinemax.common.phys;

import io.github.tt432.machinemax.MachineMax;
import net.minecraft.world.level.Level;

public class LocalPhysThread extends AbstractPhysThread {

    LocalPhysThread(Level level) {
        super(level);
    }

    //TODO:一个数组/链表，储存世界物理体快照(位置、姿态、速度、角速度)

    @Override
    public void run() {//物理计算的主线程
        while (!isInterrupted()) {//物理线程主循环
            long startTime = System.nanoTime();//记录开始时间
            //TODO:一个内部无sleep的while循环，用于根据服务器数据从数据包时间处开始快速迭代直到追上本地时间以修正误差
            regularStep(isPaused);//推进物理模拟计算进程
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
        //TODO:如有同步数据包，同步全世界物体的位姿速度
        super.regularStep(paused);
        //TODO:更新全世界物理体的快照信息
    }
}
