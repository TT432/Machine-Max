package io.github.tt432.machinemax.common.attachment;

import com.mojang.serialization.Codec;
import io.github.tt432.machinemax.MachineMax;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MMAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MachineMax.MOD_ID);
    //以下为注册的附件列表
    //方块作为道路组成部分时的碰撞箱与渲染在y轴上的偏移量
    public static final Supplier<AttachmentType<Float>> ROAD_BLOCK_OFFSET = ATTACHMENTS.register(
            "road_block_offset", () -> AttachmentType.builder(() -> 0F).serialize(Codec.FLOAT).build()
    );
}
