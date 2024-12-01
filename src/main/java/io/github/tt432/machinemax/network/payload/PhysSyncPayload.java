package io.github.tt432.machinemax.network.payload;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.utils.data.BodiesSyncData;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.HashMap;
import java.util.Map;

public record PhysSyncPayload(int step, Map<Integer, BodiesSyncData> syncData) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PhysSyncPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "phys_sync_payload"));
    public static final StreamCodec<ByteBuf, PhysSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            PhysSyncPayload::step,//同步包时间戳
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.INT,
                    BodiesSyncData.DATA_CODEC
            ),
            PhysSyncPayload::syncData,//同步的所有运动体的位姿速度信息
            PhysSyncPayload::new
    );
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
