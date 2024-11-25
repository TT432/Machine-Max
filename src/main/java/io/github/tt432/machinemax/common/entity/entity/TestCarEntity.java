package io.github.tt432.machinemax.common.entity.entity;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.part.TestCarChassisPart;
import io.github.tt432.machinemax.common.entity.controller.CarController;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class TestCarEntity extends BasicEntity {

    public TestCarEntity(EntityType<? extends BasicEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.corePart = new TestCarChassisPart(this);
        this.setController(new CarController(this));
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        return new Vec3(0.5, 0.7, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide() && this.getFirstPassenger() instanceof Player p) {
            DVector3 v = corePart.dbody.getLinearVel().copy();
            corePart.dbody.vectorFromWorld(v, v);
            p.displayClientMessage(Component.literal("速度:" + String.format("%.2f", v.get2() * 3.6) + "km/h"), true);
            this.level().addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0, 0);
        }
    }

    @Override
    public @NotNull InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        InteractionResult interactionresult = super.interact(pPlayer, pHand);
        if (interactionresult != InteractionResult.PASS) {
            return interactionresult;
        } else if (pPlayer.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else {
            if (!this.level().isClientSide) {
                return pPlayer.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return canVehicleCollide(this, pEntity);
    }

    public static boolean canVehicleCollide(Entity pVehicle, Entity pEntity) {
        return (pEntity.canBeCollidedWith() || pEntity.isPushable()) && !pVehicle.isPassengerOfSameVehicle(pEntity);
    }
}
