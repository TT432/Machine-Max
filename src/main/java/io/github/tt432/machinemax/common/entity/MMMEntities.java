package io.github.tt432.machinemax.common.entity;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entities.TestCarEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 此类为实体注册器
 * 收录了模组添加的所有实体，并定义一些基本属性
 * @author 甜粽子
 */
public class MMMEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, MachineMax.MOD_ID);
    //以下为注册的实体列表
    //测试用基本车辆
    public static final Supplier<EntityType<TestCarEntity>> TEST_CAR_ENTITY =
            ENTITIES.register("test_car",
                    () -> EntityType.Builder.of(TestCarEntity::new, MobCategory.MISC)
                            .sized(2f,2f)
                            .build("test_car"));
    //以上为注册的实体列表
}
