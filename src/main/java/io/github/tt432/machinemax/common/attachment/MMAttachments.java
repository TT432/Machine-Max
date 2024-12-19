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
    public static final Supplier<AttachmentType<Integer>> MANA = ATTACHMENTS.register(
            "mana", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );
}
