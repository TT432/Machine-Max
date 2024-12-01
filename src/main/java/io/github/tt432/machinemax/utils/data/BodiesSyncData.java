package io.github.tt432.machinemax.utils.data;

import io.github.tt432.machinemax.utils.physics.math.DQuaternion;
import io.github.tt432.machinemax.utils.physics.math.DVector3;

/**
 * 存储一个运动体的所有位姿信息
 * @param pos
 * @param rot
 * @param lVel
 * @param aVel
 */
public record BodiesSyncData(DVector3 pos, DQuaternion rot, DVector3 lVel, DVector3 aVel) {
}
