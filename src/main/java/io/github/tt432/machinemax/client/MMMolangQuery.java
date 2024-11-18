package io.github.tt432.machinemax.client;

import io.github.tt432.eyelib.molang.MolangScope;
import io.github.tt432.eyelib.molang.mapping.api.MolangFunction;
import io.github.tt432.eyelib.molang.mapping.api.MolangMapping;
import io.github.tt432.machinemax.common.entity.entity.TestCarEntity;
import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
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
        return entityFloat(scope, e -> (e instanceof TestCarEntity) ? (((TestCarEntity) e).getZRot()) : (0));
    }

    @MolangFunction(value = "part_rel_pos_x", description = "部件相对父部件的相对x坐标")
    public static float part_rel_pos_x(MolangScope scope) {
        return partFloat(scope, p -> {
            if (p instanceof AbstractPart) {
                if(p.father_part!=null){
                    DVector3 result = new DVector3(0, 0, 0);
                    p.father_part.dbody.getPosRelPoint(p.dbody.getPosition(), result);
                    return (float) result.get0();
                }else {
                    return 0F;
                }
            } else return 0F;
        });
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

    private static float partBool(MolangScope scope, ToBooleanFunction<AbstractPart> function) {
        return scope.getOwner().ownerAs(AbstractPart.class).map(l -> function.apply(l) ? TRUE : FALSE).orElse(0F);
    }

    private static float partFloat(MolangScope scope, Function<AbstractPart, Float> function) {
        return scope.getOwner().ownerAs(AbstractPart.class).map(function).orElse(0F);
    }
}
