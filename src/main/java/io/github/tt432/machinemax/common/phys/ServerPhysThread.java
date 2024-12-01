package io.github.tt432.machinemax.common.phys;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.network.payload.PhysSyncPayload;
import io.github.tt432.machinemax.utils.data.BodiesSyncData;
import io.github.tt432.machinemax.utils.physics.ode.DBody;
import io.github.tt432.machinemax.utils.physics.ode.internal.DxBody;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerPhysThread extends AbstractPhysThread {
    static final double SYNC_INTERVAL = 1;//向客户端同步世界物体位姿速度的时间间隔，秒
    public Map<Integer, BodiesSyncData> syncData = HashMap.newHashMap(100);//将要通过网络同步至各个客户端的运动体位姿速度数据

    ServerPhysThread(Level level) {
        super(level);
    }

    @Override
    public void run() {//物理计算的主线程
        while (!isInterrupted()) {//物理线程主循环
            long startTime = System.nanoTime();//记录开始时间
            regularStep(isPaused);//推进物理模拟计算进程
            if (((double) (step * STEP_SIZE) / 1000) % SYNC_INTERVAL == 0) syncBodies();//每秒进行一次与客户端的位姿速度同步
            long duration = (System.nanoTime() - startTime) / 1000000;//计算物理线程执行用时，并转换为毫秒
            long sleepTime = STEP_SIZE - duration;
            if (sleepTime < 1) sleepTime = 1;
            try {
                Thread.sleep(sleepTime);//等待
            } catch (InterruptedException e) {
                MachineMax.LOGGER.info("Stopping server phys thread...");
                contactGroup.destroy();
                space.destroy();
                world.destroy();
                break;
            }
        }
    }

    /**
     * 将物理线程内
     */
    protected void syncBodies() {
        syncData.clear();
        for (Iterator<DBody> it = world.getBodyIterator(); it.hasNext(); ) {//记录本维度内每个运动体的位置、姿态、速度和角速度
            DxBody b = (DxBody) it.next();
            syncData.put(b.getId(), new BodiesSyncData(b.getPosition().copy(), b.getQuaternion().copy(), b.getLinearVel().copy(), b.getAngularVel().copy()));
        }
        if (!syncData.isEmpty())//维度存在运动体则将信息同步给维度内的玩家
            PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new PhysSyncPayload(step, syncData));
    }
}