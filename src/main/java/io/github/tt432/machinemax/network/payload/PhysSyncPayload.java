package io.github.tt432.machinemax.network.payload;

import io.github.tt432.machinemax.MachineMax;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PhysSyncPayload implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GroundInputPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MachineMax.MOD_ID, "phys_sync_payload"));
    //TODO:写物理体的位置、姿态、速度、角速度同步
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
