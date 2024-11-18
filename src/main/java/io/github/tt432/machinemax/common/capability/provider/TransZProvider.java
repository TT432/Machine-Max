package io.github.tt432.machinemax.common.capability.provider;

import io.github.tt432.machinemax.common.capability.capability.TransZInput;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public class TransZProvider implements ICapabilityProvider<BasicEntity,Void, TransZInput>, INBTSerializable<CompoundTag> {
    private TransZInput input;

    private TransZInput create(){
        if (this.input ==null) this.input =new TransZInput();
        return input;
    }
    // 实现 ICapabilityProvider 接口的 getCapability 方法，用于获取能力实例
    @Override
    public @Nullable TransZInput getCapability(BasicEntity object, Void context) {
        return create();
    }
    // 实现 INBTSerializable 接口的 serializeNBT 方法，用于将能力值保存到 NBT 数据中
    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        input.saveNBTData(nbt);
        return nbt;
    }
    // 实现 INBTSerializable 接口的 deserializeNBT 方法，用于从NBT中加载能力值
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        create().loadNBTData(nbt);
    }
}
