package io.github.tt432.machinemax.client;

import io.github.tt432.eyelib.molang.MolangScope;
import io.github.tt432.eyelib.molang.mapping.api.MolangFunction;
import io.github.tt432.eyelib.molang.mapping.api.MolangMapping;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.common.entity.entity.TestCarEntity;
import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.common.part.TestCarWheelPart;
import io.github.tt432.machinemax.util.physics.math.DMatrix3;
import io.github.tt432.machinemax.util.physics.math.DQuaternion;
import io.github.tt432.machinemax.util.physics.math.DVector3;
import io.github.tt432.machinemax.util.physics.ode.internal.Rotation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

import static io.github.tt432.eyelib.molang.MolangValue.FALSE;
import static io.github.tt432.eyelib.molang.MolangValue.TRUE;

/**
 * @author 甜粽子
 */
@MolangMapping(value = "query", pureFunction = false)
public final class MMMolangQuery {

    @MolangFunction(value = "roll", description = "roll 角度（z rot）")
    public static float roll(MolangScope scope) {
        return entityFloat(scope, e -> (e instanceof BasicEntity) ? (((BasicEntity) e).getZRot()) : (0));
    }

    @FunctionalInterface
    interface ToBooleanFunction<K> {
        boolean apply(K key);
    }

    private static float entityBool(MolangScope scope, ToBooleanFunction<Entity> function) {
        return scope.getOwner().ownerAs(Entity.class).map(l -> function.apply(l) ? TRUE : FALSE).orElse(0F);
    }

    private static float entityFloat(MolangScope scope, Function<Entity, Float> function) {
        return scope.getOwner().ownerAs(Entity.class).map(function).orElse(0F);
    }

    private static float livingBool(MolangScope scope, ToBooleanFunction<LivingEntity> function) {
        return scope.getOwner().ownerAs(LivingEntity.class)
                .map(l -> function.apply(l) ? TRUE : FALSE)
                .orElse(0F);
    }

    private static float livingFloat(MolangScope scope, Function<LivingEntity, Float> function) {
        return scope.getOwner().ownerAs(LivingEntity.class).map(function).orElse(0F);
    }
}
