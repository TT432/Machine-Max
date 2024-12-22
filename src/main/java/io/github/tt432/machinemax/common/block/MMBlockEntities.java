package io.github.tt432.machinemax.common.block;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.block.road.RoadBaseBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MMBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MachineMax.MOD_ID);
    //路基方块实体
    public static final Supplier<BlockEntityType<RoadBaseBlockEntity>> ROAD_BASE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "road_base_block_entity",
            ()-> BlockEntityType.Builder.of(
                    RoadBaseBlockEntity::new,
                    MMBlocks.ROAD_BASE_BLOCK.get()
            ).build(null));
}
