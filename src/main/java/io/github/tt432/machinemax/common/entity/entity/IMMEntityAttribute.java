package io.github.tt432.machinemax.common.entity.entity;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public interface IMMEntityAttribute {
    //注册属性
    static AttributeSupplier.Builder createLivingAttributes() {
        return AttributeSupplier.builder()
                .add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED, 0D)
                .add(Attributes.ARMOR)
                .add(Attributes.ARMOR_TOUGHNESS)
                .add(Attributes.MAX_ABSORPTION)
                .add(Attributes.STEP_HEIGHT,0D)
                .add(Attributes.SCALE, 1D)
                .add(Attributes.GRAVITY,0D)//屏蔽原版重力，交由物理引擎控制
                .add(Attributes.SAFE_FALL_DISTANCE)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER,0D)//屏蔽原版掉落伤害
                .add(Attributes.JUMP_STRENGTH, 0D)
                .add(Attributes.OXYGEN_BONUS)
                .add(Attributes.BURNING_TIME, 0D)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY)
                .add(Attributes.MOVEMENT_EFFICIENCY)
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(net.neoforged.neoforge.common.NeoForgeMod.SWIM_SPEED)
                .add(net.neoforged.neoforge.common.NeoForgeMod.NAMETAG_DISTANCE);
    }
}
