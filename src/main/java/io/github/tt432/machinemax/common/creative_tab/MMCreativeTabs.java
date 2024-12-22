package io.github.tt432.machinemax.common.creative_tab;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.block.MMBlocks;
import io.github.tt432.machinemax.common.item.MMItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MMCreativeTabs {
    // 对应的注册器
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MachineMax.MOD_ID);
    // 这个string是鼠标移动到tab上的显示的内容。
    public static final String EXAMPLE_TAB_STRING = "machine_max.example_tab";
    // 添加一个tab，title标题，icon显示图标，displayItem是指tab中添加的内容，这里传入一个lammabd表达式，通过poutput添加物品
    public static final Supplier<CreativeModeTab> EXAMPLE_TAB  = CREATIVE_MODE_TABS.register("example_tab",() -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable(EXAMPLE_TAB_STRING))
            .icon(()-> MMItems.TEST_CAR_SPAWNER.get().getDefaultInstance())
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(MMItems.TEST_CAR_SPAWNER.get());
//                pOutput.accept(MMItems.ROAD_BASE_BLOCK.get());
            })
            .build());
}
