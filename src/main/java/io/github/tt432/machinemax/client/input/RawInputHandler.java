package io.github.tt432.machinemax.client.input;

import io.github.tt432.machinemax.MachineMax;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = MachineMax.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
@OnlyIn(Dist.CLIENT)
public class RawInputHandler {

    /**
     * 在每个客户端tick事件后调用，处理按键逻辑。
     * @param event 客户端tick事件对象
     */
    @SubscribeEvent
    public static void handleKeyInputs(ClientTickEvent.Post event) {
        var client = Minecraft.getInstance();
        if (KeyBinding.groundForwardKey.isDown()) {
            // 按键被按下时的处理逻辑
            client.player.displayClientMessage(Component.literal("地面前进"),true);
        }
    }

}