package io.github.tt432.machinemax.common.item;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.item.prop.TestCarSpawnerItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MMItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MachineMax.MOD_ID);

    public static final Supplier<Item> TEST_CAR_SPAWNER =
            ITEMS.register(
                    "test_car_spawner",
                    ()->new TestCarSpawnerItem(new Item.Properties()));
}