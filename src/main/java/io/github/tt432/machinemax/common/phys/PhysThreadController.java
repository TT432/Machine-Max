package io.github.tt432.machinemax.common.phys;

import io.github.tt432.machinemax.MachineMax;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkTicketLevelUpdatedEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import static io.github.tt432.machinemax.MachineMax.MOD_ID;

public class PhysThreadController {

    public static PhysThread physThread;
    public static PhysThread localPhysThread;

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class events{
        @SubscribeEvent//加载世界时开启新物理计算线程
        public static void localPhysThreadStart(LevelEvent.Load event){//这个会执行三次，大概是原版三个维度？
            if(event.getLevel().isClientSide()){
                MachineMax.LOGGER.info("Preparing local phys thread...");
                localPhysThread = new PhysThread();
                localPhysThread.start();//启动本地线程！
                localPhysThread.setName("Local physics thread");
            }else {
                //TODO:把服务器物理引擎线程放到这里
                //TODO:每个维度一个物理引擎线程
            }
        }
        @SubscribeEvent @Deprecated
        public static void physThreadStart(ServerAboutToStartEvent event){
            MachineMax.LOGGER.info("Preparing phys thread...");
            physThread = new PhysThread();
            physThread.start();//启动服务器线程！
            physThread.setName("Physics thread");
        }
        @SubscribeEvent//卸载世界时停止物理引擎线程
        public static void physThreadStop(LevelEvent.Unload event){
            if (event.getLevel().isClientSide()){
                localPhysThread.interrupt();//结束线程
                MachineMax.LOGGER.info("Local phys thread Stopped.");
            }else {
                physThread.interrupt();//结束线程
                MachineMax.LOGGER.info("Server phys thread Stopped.");
            }
        }

        @SubscribeEvent//TODO:根据主线程同步物理引擎线程时间
        public static void physThreadSynchronize(ServerTickEvent.Pre event){

        }
        @SubscribeEvent//TODO:根据区块方块分布更新物理演算用地形碰撞
        public static void phyThreadMapUpdate(ChunkTicketLevelUpdatedEvent event){

        }
    }
}
