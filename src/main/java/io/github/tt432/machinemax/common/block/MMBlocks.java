package io.github.tt432.machinemax.common.block;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.block.road.RoadBaseBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MMBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MachineMax.MOD_ID);
    //路基方块
    public static final DeferredBlock<Block> ROAD_BASE_BLOCK = BLOCKS.register(
            "road_base_block",
            () -> new RoadBaseBlock(BlockBehaviour.Properties.of()
                    .destroyTime(5.0f)
                    .explosionResistance(10.0f)
                    .sound(SoundType.COPPER)
                    .friction(0.5f)
                    .speedFactor(1.0f)
            ));
}
