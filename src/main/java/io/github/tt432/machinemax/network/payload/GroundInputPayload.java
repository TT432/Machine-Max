package io.github.tt432.machinemax.network.payload;

import io.github.tt432.machinemax.MachineMax;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record GroundInputPayload(int key, float time) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GroundInputPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "ground_input_payload"));
    public static final StreamCodec<ByteBuf, GroundInputPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            GroundInputPayload::key,//按下的按键
            ByteBufCodecs.FLOAT,
            GroundInputPayload::time,//按键的持续时间，按键刚被按下时为0，松开时则返回持续时间
            GroundInputPayload::new
    );
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
