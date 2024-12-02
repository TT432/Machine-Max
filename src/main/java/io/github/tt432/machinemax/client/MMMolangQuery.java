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

    //TODO:作用域为部件的位姿信息Molang，暂时无效
    @MolangFunction(value = "part_rel_pos_x", description = "部件相对父部件的相对x坐标(m)")
    public static float part_rel_pos_x(MolangScope scope) {
        return partFloat(scope, p -> {
            if (p instanceof AbstractPart) {
                if (p.fatherPart != null) {
                    DVector3 result = new DVector3(0, 0, 0);
                    p.fatherPart.dbody.getPosRelPoint(p.dbody.getPosition(), result);//计算相对父部件的位移
                    return (float) result.get0();//返回x值
                } else {
                    return 0F;
                }
            } else return 0F;
        });
    }

    @MolangFunction(value = "part_rel_pos_y", description = "部件相对父部件的相对y坐标(m)")
    public static float part_rel_pos_y(MolangScope scope) {
        return partFloat(scope, p -> {
            if (p instanceof AbstractPart) {
                if (p.fatherPart != null) {
                    DVector3 result = new DVector3(0, 0, 0);
                    p.fatherPart.dbody.getPosRelPoint(p.dbody.getPosition(), result);//计算相对父部件的位移
                    return (float) result.get1();//返回y值
                } else {
                    return 0F;
                }
            } else return 0F;
        });
    }

    @MolangFunction(value = "part_rel_pos_z", description = "部件相对父部件的相对z坐标(m)")
    public static float part_rel_pos_z(MolangScope scope) {
        return partFloat(scope, p -> {
            if (p instanceof AbstractPart) {
                if (p.fatherPart != null) {
                    DVector3 result = new DVector3(0, 0, 0);
                    p.fatherPart.dbody.getPosRelPoint(p.dbody.getPosition(), result);//计算相对父部件的位移
                    return (float) result.get2();//返回z值
                } else {
                    return 0F;
                }
            } else return 0F;
        });
    }
    //TODO:部件相对父部件的旋转，下为临时代用，作用域全实体，检测右前轮姿态
    @MolangFunction(value = "part_rel_rot_x", description = "pitch 角度（x rot）")
    public static float part_rel_rot_x(MolangScope scope) {
        return entityFloat(scope, e -> {
            if(e instanceof TestCarEntity && ((TestCarEntity) e).corePart != null){
                TestCarWheelPart part = (TestCarWheelPart) ((TestCarEntity) e).corePart.childrenPartSlots.get(0).getChildPart();
                DMatrix3 temp = new DMatrix3();
                DMatrix3 rot1 = new DMatrix3();
                rot1.eqMul(//计算部件相对父部件旋转
                        part.fatherPart.dbody.getRotation().copy().reTranspose(),
                        part.dbody.getRotation().copy());
                Rotation.dRFromEulerAngles(temp,180,0,0);
                rot1.eqMul(temp,rot1);//绕X轴旋转180°以匹配Blockbench旋转坐标系(X左Y下Z后)
                DQuaternion dq=new DQuaternion();
                Rotation.dQfromR(dq,rot1);
                DVector3 ang = dq.toEulerDegreesZYX();//Blockbench欧拉角旋转顺序
                return (float) ang.get0();
            }else return 0F;
        });
    }
    @MolangFunction(value = "part_rel_rot_y", description = "yaw 角度（y rot）")
    public static float part_rel_rot_y(MolangScope scope) {
        return entityFloat(scope, e -> {
            if(e instanceof TestCarEntity && ((TestCarEntity) e).corePart != null){
                TestCarWheelPart part = (TestCarWheelPart) ((TestCarEntity) e).corePart.childrenPartSlots.get(0).getChildPart();
                DMatrix3 temp = new DMatrix3();
                DMatrix3 rot1 = new DMatrix3();
                rot1.eqMul(//计算部件相对父部件旋转
                        part.fatherPart.dbody.getRotation().copy().reTranspose(),
                        part.dbody.getRotation().copy());
                Rotation.dRFromEulerAngles(temp,180,0,0);
                rot1.eqMul(temp,rot1);//绕X轴旋转180°以匹配Blockbench旋转坐标系(X左Y下Z后)
                DQuaternion dq=new DQuaternion();
                Rotation.dQfromR(dq,rot1);
                DVector3 ang = dq.toEulerDegreesZYX();//Blockbench欧拉角旋转顺序
                return (float) ang.get1();
            }else return 0F;
        });
    }
    @MolangFunction(value = "part_rel_rot_z", description = "roll 角度（z rot）")
    public static float part_rel_rot_z(MolangScope scope) {
        return entityFloat(scope, e -> {
            if(e instanceof TestCarEntity && ((TestCarEntity) e).corePart != null){
                TestCarWheelPart part = (TestCarWheelPart) ((TestCarEntity) e).corePart.childrenPartSlots.get(0).getChildPart();
                DMatrix3 temp = new DMatrix3();
                DMatrix3 rot1 = new DMatrix3();
                rot1.eqMul(//计算部件相对父部件旋转
                        part.fatherPart.dbody.getRotation().copy().reTranspose(),
                        part.dbody.getRotation().copy());
                Rotation.dRFromEulerAngles(temp,180,0,0);
                rot1.eqMul(temp,rot1);//绕X轴旋转180°以匹配Blockbench旋转坐标系(X左Y下Z后)
                DQuaternion dq=new DQuaternion();
                Rotation.dQfromR(dq,rot1);
                DVector3 ang = dq.toEulerDegreesZYX();//Blockbench欧拉角旋转顺序
                return (float) ang.get2();
            }else return 0F;
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
