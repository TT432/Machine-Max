package io.github.tt432.machinemax.common.phys;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.mixin_interface.IMixinLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkTicketLevelUpdatedEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

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

        @SubscribeEvent//TODO:根据区块方块分布更新物理演算用地形碰撞
        public static void phyThreadMapUpdate(ChunkTicketLevelUpdatedEvent event) {

        }
    }
}
