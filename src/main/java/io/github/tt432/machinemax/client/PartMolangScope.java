package io.github.tt432.machinemax.client;

import io.github.tt432.eyelib.capability.EyelibAttachableData;
import io.github.tt432.eyelib.molang.MolangScope;
import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.util.physics.math.DMatrix3;
import io.github.tt432.machinemax.util.physics.math.DQuaternion;
import io.github.tt432.machinemax.util.physics.math.DVector3;
import io.github.tt432.machinemax.util.physics.ode.internal.Rotation;
import lombok.Getter;

public class PartMolangScope {

    AbstractPart part;
    @Getter
    public final MolangScope scope = new MolangScope();
    @Getter
    private float worldX=0;
    @Getter
    private float worldY=0;
    @Getter
    private float worldZ=0;
    @Getter
    private float x=0;
    @Getter
    private float y=0;
    @Getter
    private float z=0;
    @Getter
    private float pitch=0;
    @Getter
    private float yaw=0;
    @Getter
    private float roll=0;

    public PartMolangScope(AbstractPart part) {
        this.part = part;
        scope.setOwner(part);
        scope.setParent(part.getAttachedEntity().getData(EyelibAttachableData.RENDER_DATA).getScope());
        //TODO:注意！使用时必须使用variable前缀，不可使用v.的缩写！
        scope.set("variable.part_world_x", this::getWorldX);
        scope.set("variable.part_world_y", this::getWorldY);
        scope.set("variable.part_world_z", this::getWorldZ);
        scope.set("variable.part_rel_x", this::getX);
        scope.set("variable.part_rel_y", this::getY);
        scope.set("variable.part_rel_z", this::getZ);
        scope.set("variable.part_pitch", this::getPitch);
        scope.set("variable.part_yaw", this::getYaw);
        scope.set("variable.part_roll", this::getRoll);
        scope.set("variable.part_health", this::getHealth);
        scope.set("variable.part_max_health", this::getMaxHealth);
        scope.set("variable.part_armor", this::getArmor);
        scope.set("variable.part_max_armor", this::getMaxArmor);
    }

    public float getMaxArmor() {
        return (float) part.getArmor();
    }

    public float getArmor() {
        return (float) part.getArmor();
    }

    public float getMaxHealth() {
        return (float) part.getMaxHealth();
    }

    public float getHealth() {
        return (float) part.getHealth();
    }

    public void updatePhysMolang() {
        DVector3 result;
        result = part.dbody.getPosition().copy();
        this.worldX = (float) result.get0();
        this.worldY = (float) result.get1();
        this.worldZ = (float) result.get2();
        if (part.fatherPart == null) {//如果是根部件则不计算相对位置
            this.x = 0;
            this.y = 0;
            this.z = 0;
        } else {//否则计算相对父部件的位置
            part.fatherPart.dbody.getPosRelPoint(part.dbody.getPosition().copy(), result);//计算相对父部件的位移
            result.sub(part.attachedSlot.getChildPartAttachPos());//减去初始安装位置的影响
            this.x = (float) result.get0();
            this.y = (float) result.get1();
            this.z = (float) result.get2();
        }

        DMatrix3 temp = new DMatrix3();
        DMatrix3 rot1 = new DMatrix3();
        if (part.fatherPart != null) {
            rot1.eqMul(//计算部件相对父部件旋转
                    part.fatherPart.dbody.getRotation().copy().reTranspose(),
                    part.dbody.getRotation().copy());
        } else {
            rot1 = new DMatrix3().setIdentity();
        }
        Rotation.dRFromEulerAngles(temp, 180, 0, 0);
        rot1.eqMul(temp, rot1);//绕X轴旋转180°以匹配Blockbench旋转坐标系(X左Y下Z后)
        DQuaternion dq = new DQuaternion();
        Rotation.dQfromR(dq, rot1);
        DVector3 ang = dq.toEulerDegreesZYX();//Blockbench欧拉角旋转顺序
        this.pitch = (float) ang.get0();
        this.yaw = (float) ang.get1();
        this.roll = (float) ang.get2();
    }

}
