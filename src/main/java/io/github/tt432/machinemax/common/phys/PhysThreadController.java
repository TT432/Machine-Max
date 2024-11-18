package io.github.tt432.machinemax.common.phys;

import io.github.tt432.machinemax.MachineMax;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.level.ChunkTicketLevelUpdatedEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import static io.github.tt432.machinemax.MachineMax.MOD_ID;

public class PhysThreadController {

    public static PhysThread physThread;
    public static LocalPhysThread localPhysThread;

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class events{
        //TODO:每个维度一个物理线程
        @SubscribeEvent//加载世界时开启新物理计算线程
        //public static void physThreadStart(LevelEvent.Load event){//这个会执行三次，大概是原版三个维度？
        public static void physThreadStart(ServerAboutToStartEvent event){
            MachineMax.LOGGER.info("Preparing phys thread...");
            physThread = new PhysThread();
            physThread.start();//启动线程！
            physThread.setName("Physics thread");
        }
        @SubscribeEvent//卸载世界时停止物理计算线程
        public static void physThreadStop(LevelEvent.Unload event){
            if (!event.getLevel().isClientSide()){
                physThread.interrupt();
                MachineMax.LOGGER.info("Server phys thread Stopped.");
            }else {
                //localPhysThread.interrupt();
                MachineMax.LOGGER.info("Local phys thread Stopped.");
            }
        }

        @SubscribeEvent//TODO:根据主线程同步物理线程时间
        public static void physThreadSynchronize(ServerTickEvent.Pre event){

        }
        @SubscribeEvent//TODO:根据区块方块分布更新物理演算用地形碰撞
        public static void phyThreadMapUpdate(ChunkTicketLevelUpdatedEvent event){

        }
    }
}
