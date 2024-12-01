package io.github.tt432.machinemax.network;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.network.handler.InputPayloadHandler;
import io.github.tt432.machinemax.network.handler.MoveInputPayloadHandler;
import io.github.tt432.machinemax.network.handler.PhysSyncPayloadHandler;
import io.github.tt432.machinemax.network.payload.PhysSyncPayload;
import io.github.tt432.machinemax.network.payload.RegularInputPayload;
import io.github.tt432.machinemax.network.payload.MovementInputPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MachineMax.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MMPayloadRegistry {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("1.0.1");
        //注册网络包及其处理
        registrar.playBidirectional(//移动输入
                MovementInputPayload.TYPE,
                MovementInputPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        MoveInputPayloadHandler::clientHandler,
                        MoveInputPayloadHandler::serverHandler
                )
        );
        registrar.playBidirectional(//常规输入
                RegularInputPayload.TYPE,
                RegularInputPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        InputPayloadHandler::clientHandler,
                        InputPayloadHandler::serverHandler
                )
        );
        registrar.playToClient(//运动体的位姿和速度
                PhysSyncPayload.TYPE,
                PhysSyncPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(PhysSyncPayloadHandler::handler)
        );
    }
}
