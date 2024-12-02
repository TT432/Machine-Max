package io.github.tt432.machinemax;

import com.mojang.logging.LogUtils;
import io.github.tt432.machinemax.common.creative_tab.MMCreativeTabs;
import io.github.tt432.machinemax.common.entity.MMEntities;
import io.github.tt432.machinemax.common.item.MMItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

import static io.github.tt432.machinemax.MachineMax.MOD_ID;

@Mod(MOD_ID)
public class MachineMax {

    public static final String MOD_ID = "machine_max";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MachineMax(IEventBus bus){
        MMEntities.ENTITIES.register(bus);//注册所有实体
        MMItems.ITEMS.register(bus);//注册所有物品
        MMCreativeTabs.CREATIVE_MODE_TABS.register(bus);//注册所有创造模式物品栏
    }

}