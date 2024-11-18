package io.github.tt432.machinemax.network.handler;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.network.payload.MoveInputPayload;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Arrays;

public class MoveInputPayloadHandler {

    public static void clientHandler(final MoveInputPayload payload, final IPayloadContext context) {
        //将其他玩家的输入同步至本机，以在客户端模拟其他玩家的操作
        Player player = context.player();
        if (player.getVehicle() instanceof BasicEntity e && e.getController() != null) {
            e.getController().setRawMoveInput(payload.input());
            e.getController().setMoveInputConflict(payload.inputConflict());
        }
    }

    public static void serverHandler(final MoveInputPayload payload, final IPayloadContext context) {
        Player player = context.player();
        if (player.getVehicle() instanceof BasicEntity e && e.getController() != null) {
            e.getController().setRawMoveInput(payload.input());
            e.getController().setMoveInputConflict(payload.inputConflict());
        }
        //将玩家输入转发给其他玩家，以在其他玩家客户端模拟自己的操作
        PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), payload);
    }
}
