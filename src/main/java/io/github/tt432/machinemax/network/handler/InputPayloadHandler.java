package io.github.tt432.machinemax.network.handler;

import io.github.tt432.machinemax.network.payload.RegularInputPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class InputPayloadHandler {
    //将其他玩家的输入同步至本机，以在客户端模拟其他玩家的操作
    public static void clientHandler(final RegularInputPayload payload, final IPayloadContext context){

    }
    public static void serverHandler(final RegularInputPayload payload, final IPayloadContext context){

    }
}
