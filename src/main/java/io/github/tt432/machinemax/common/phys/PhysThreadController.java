package io.github.tt432.machinemax.common.phys;

import cn.solarmoon.spark_core.api.phys.thread.ClientPhysLevel;
import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.mixin_interface.IMixinLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.ChunkTicketLevelUpdatedEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.ode4j.ode.DGeom;

import static io.github.tt432.machinemax.MachineMax.MOD_ID;

public class PhysThreadController {

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class events {
        @SubscribeEvent//加载世界时开启新物理计算线程
        public static void localPhysThreadStart(LevelEvent.Load event) {//这个会执行三次，大概是原版三个维度？
            if (event.getLevel().isClientSide()) {
                MachineMax.LOGGER.info("Preparing local phys thread...");
                IMixinLevel level = ((IMixinLevel) event.getLevel());
                level.machine_Max$setPhysThread(new LocalPhysThread((Level) event.getLevel()));
                level.machine_Max$getPhysThread().start();//启动本地线程！
                level.machine_Max$getPhysThread().setName("Local physics thread");
            } else {
                MachineMax.LOGGER.info("Preparing phys thread...");
                IMixinLevel level = ((IMixinLevel) event.getLevel());
                level.machine_Max$setPhysThread(new ServerPhysThread((Level) event.getLevel()));
                level.machine_Max$getPhysThread().start();//启动本地线程！
                level.machine_Max$getPhysThread().setName(level + "Physics thread");
            }
        }

        @SubscribeEvent//卸载世界时停止物理引擎线程
        public static void physThreadStop(LevelEvent.Unload event) {
            IMixinLevel level = (IMixinLevel) event.getLevel();
            level.machine_Max$getPhysThread().interrupt();//结束线程
            if (event.getLevel().isClientSide()) {
                MachineMax.LOGGER.info("Local phys thread Stopped.");
            } else {
                MachineMax.LOGGER.info("Server phys thread Stopped.");
            }
        }

        @SubscribeEvent//TODO:根据主线程同步物理引擎线程时间
        public static void physThreadSynchronize(ServerTickEvent.Pre event) {

        }

        @SubscribeEvent//加载区块时创建或加载地形碰撞箱
        public static void phyThreadMapUpdate(ChunkEvent.Load event) {
//            TerrainBuilder.build(event.getChunk());
            //TODO:根据区块变化更新区块储存的Trimesh数据，以免无限创建新几何体导致内存溢出
            //TODO:优化碰撞时间效率
            //TODO:优化内存占用
            //似乎没有必要了，现场创建Box的效果也不错
        }

        @SubscribeEvent//卸载区块时销毁地形碰撞箱，保存构造数据
        public static void phyThreadMapUnload(ChunkEvent.Unload event) {
            //TODO:随区块卸载销毁Trimesh几何体，但保留构造数据
        }
    }
}
