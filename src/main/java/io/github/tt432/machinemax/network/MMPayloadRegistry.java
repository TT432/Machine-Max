package io.github.tt432.machinemax.network;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.network.handler.GroundInputPayloadHandler;
import io.github.tt432.machinemax.network.payload.GroundInputPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MachineMax.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MMPayloadRegistry {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("1.0.0");
        //注册网络包及其处理
        registrar.playBidirectional(
                GroundInputPayload.TYPE,
                GroundInputPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        GroundInputPayloadHandler::clientHandler,
                        GroundInputPayloadHandler::serverHandler
                )
        );
    }
}
