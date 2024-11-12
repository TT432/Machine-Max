package io.github.tt432.machinemax.network.handler;

import io.github.tt432.machinemax.network.payload.GroundInputPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class GroundInputPayloadHandler {
    public static void clientHandler(final GroundInputPayload payload, final IPayloadContext context){
        int i = payload.key();
    }
    public static void serverHandler(final GroundInputPayload payload, final IPayloadContext context){

    }
}
