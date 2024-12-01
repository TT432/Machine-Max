package io.github.tt432.machinemax.utils.data;

import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DVector3;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * 存储一个运动体的所有位姿速度信息。
 * <p>
 * Storage all posture and velocity info of a body.
 *
 * @param pos  位置坐标 Position
 * @param rot  四元数形式的旋转姿态 Rotation in quaternion
 * @param lVel 线速度 Liner velocity
 * @param aVel 角速度 Angular velocity
 */
public record BodiesSyncData(DVector3 pos, DQuaternion rot, DVector3 lVel, DVector3 aVel) {
    /**
     * 用于网络发包传输运动体位姿速度信息的编解码器
     * <p>
     * Stream codec for body phys sync payload delivering.
     */
    public static final StreamCodec<ByteBuf, BodiesSyncData> DATA_CODEC = new StreamCodec<>() {
        public BodiesSyncData decode(ByteBuf data) {
            DVector3 pos = new DVector3(data.readDouble(), data.readDouble(), data.readDouble());//解码位置 Decode position
            DQuaternion rot = new DQuaternion(data.readDouble(), data.readDouble(), data.readDouble(), data.readDouble());//解码姿态 Decode rotation
            DVector3 lVel = new DVector3(data.readDouble(), data.readDouble(), data.readDouble());//解码线速度 Decode liner velocity
            DVector3 aVel = new DVector3(data.readDouble(), data.readDouble(), data.readDouble());//解码角速度 Decode angular velocity
            BodiesSyncData result = new BodiesSyncData(pos, rot, lVel, aVel);
            return result;
        }

        public void encode(ByteBuf buffer, BodiesSyncData data) {
            buffer.writeDouble(data.pos.get0());//编码位置 Encode position
            buffer.writeDouble(data.pos.get1());
            buffer.writeDouble(data.pos.get2());
            buffer.writeDouble(data.rot.get0());//编码姿态 Encode rotation
            buffer.writeDouble(data.rot.get1());
            buffer.writeDouble(data.rot.get2());
            buffer.writeDouble(data.rot.get3());
            buffer.writeDouble(data.lVel.get0());//编码线速度 Encode liner velocity
            buffer.writeDouble(data.lVel.get1());
            buffer.writeDouble(data.lVel.get2());
            buffer.writeDouble(data.aVel.get0());//编码角速度 Encode Angular velocity
            buffer.writeDouble(data.aVel.get1());
            buffer.writeDouble(data.aVel.get2());
        }
    };
}
