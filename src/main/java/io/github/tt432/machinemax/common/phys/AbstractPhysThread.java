package io.github.tt432.machinemax.common.phys;

import cn.solarmoon.spark_core.api.phys.PhysWorld;
import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entity.PartEntity;
import io.github.tt432.machinemax.common.part.AbstractPart;
import io.github.tt432.machinemax.util.data.BodiesSyncData;
import org.ode4j.math.DVector3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.ode4j.ode.*;

import java.util.*;

import static org.ode4j.ode.OdeConstants.*;
import static org.ode4j.ode.OdeHelper.areConnectedExcluding;
import static org.ode4j.ode.OdeMath.dxSafeNormalize3;

abstract public class AbstractPhysThread extends Thread {
    public final Level level;//物理计算线程与每个维度绑定，即每个维度都有一个物理计算线程
    public final DWorld world;//容纳所有碰撞体与运动体的世界
    public Map<Integer, BodiesSyncData> syncData = HashMap.newHashMap(100);//用于同步的线程内所有运动体位姿速度数据
    protected Map<BlockPos, BlockState> terrainCollisionBlocks = HashMap.newHashMap(100);//用于碰撞检测的地形块位置集合
    public volatile DSpace space;
    public ArrayList<DGeom> terrainGeoms = new ArrayList<>();
    public volatile DJointGroup contactGroup;
    public volatile boolean isPaused = false;
    public static final long STEP_SIZE = 20;//物理线程计算步长(毫秒)
    protected volatile int step = 0;//物理运算迭代运行的总次数
    PhysWorld w = new PhysWorld(2L);

    AbstractPhysThread(Level level) {
        this.level = level;
        OdeHelper.initODE();//初始化物理库
        world = OdeHelper.createWorld();
        init();
    }

    abstract public void run();

    /**
     * 初始化物理模拟线程，设置仿真基本参数
     */
    protected void init() {
        isPaused = false;
        MachineMax.LOGGER.info("New phys thread started!");
        world.setGravity(0, -9.81, 0);//设置重力
//        world.setContactSurfaceLayer(0.01);//最大陷入深度，有助于防止抖振(虽然本来似乎也没)
        world.setERP(0.3);
        world.setCFM(0.00005);
        world.setAutoDisableFlag(true);//设置静止物体自动休眠以节约性能
        world.setAutoDisableSteps(5);
        world.setQuickStepNumIterations(40);//设定迭代次数以提高物理计算精度
        world.setQuickStepW(1.3);
        world.setContactMaxCorrectingVel(20);
        //TODO:区分碰撞空间(常规)，命中判定空间(弹头刀刃等放进来)和自体碰撞空间(头发布料等有物理没碰撞的放进来)
        space = OdeHelper.createHashSpace();//碰撞空间，用于容纳各类碰撞体
        contactGroup = OdeHelper.createJointGroup();
        OdeHelper.createPlane(space, 0, 1, 0, -64);//创造碰撞平面
        step = 0;
    }

    /**
     * 在物理仿真未处于暂停状态时推进主仿真进程
     *
     * @param paused -是否暂停了物理仿真进程
     */
    protected void regularStep(boolean paused) {
        internalStep(paused);
        step++;
        //TODO:处理几何体和运动体的增删
    }

    /**
     * 在物理仿真未处于暂停状态时推进仿真进程，不进行运动体和碰撞体的增删处理
     *
     * @param paused -是否暂停了物理仿真进程
     */
    protected void internalStep(boolean paused) {
        if (!paused) {
            addTerrainCollisionBoxes();//读取所有与Body绑定了的Geom周围的方块，为其添加碰撞盒
            space.collide(null, nearCallback);//碰撞检测
            world.quickStep((double) STEP_SIZE / 1000);
            contactGroup.empty();//碰撞处理完成后移除所有碰撞点约束
        }
    }

    protected void addTerrainCollisionBoxes() {
        terrainCollisionBlocks.clear();
        for (DGeom geom : space.getGeoms()) {
            if (geom.getBody() != null) {
                DAABBC aabb = geom.getAABB();
                int minX = (int) Math.floor(aabb.getMin0() - 1);
                int maxX = (int) Math.ceil(aabb.getMax0()) + 1;
                int minY = (int) Math.floor(aabb.getMin1());
                int maxY = (int) Math.ceil(aabb.getMax1());
                int minZ = (int) Math.floor(aabb.getMin2() - 1);
                int maxZ = (int) Math.ceil(aabb.getMax2() + 1);

                for (int x = minX; x <= maxX; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            BlockPos pos = new BlockPos(x, y, z);
                            BlockState state = level.getBlockState(pos);
                            if (!state.getCollisionShape(level, pos).isEmpty()) {
                                // 如果块不是空气或可替换方块，记录方块的状态和坐标
                                terrainCollisionBlocks.put(pos, state);
                            }
                        }
                    }
                }
            }
        }
        int blockNum = terrainCollisionBlocks.size();
        if (blockNum > 0) {
            if (terrainGeoms.size() < blockNum) {//所需方块数量大于现有碰撞体数量时，创建新的碰撞体
                int i = blockNum - terrainGeoms.size();
                DGeom[] geoms = new DGeom[i];
                for (int j = 0; j < i; j++) {
                    geoms[j] = OdeHelper.createBox(space, 1, 1, 1);
                    geoms[j].setPosition(0, -512, 0);
                    terrainGeoms.add(geoms[j]);
                }
            }
            int i = 0;
            for (BlockPos pos : terrainCollisionBlocks.keySet()) {
                terrainGeoms.get(i).setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                i++;
            }
        }
    }

    protected DGeom.DNearCallback nearCallback = this::nearCallback;

    protected void nearCallback(Object data, DGeom o1, DGeom o2) {
        //MachineMax.LOGGER.info("NearCallback called!");
        //exit without doing anything if the two bodies are connected by a joint
        if (o1 instanceof DTriMesh && o2 instanceof DTriMesh) return;
        DBody b1 = o1.getBody();
        DBody b2 = o2.getBody();
        if (b1 != null && b2 != null && areConnectedExcluding(b1, b2, DJoint.class)) {
            return;
        }
        PartEntity e1;
        PartEntity e2;
        if (b1 != null && b2 != null) {
            e1 = b1.getOwner().getAttachedEntity();
            e2 = b2.getOwner().getAttachedEntity();
            if (e1 == e2) {
                return;
            }
        }
        int contactNum = 64;
        DContactBuffer contacts = new DContactBuffer(contactNum);   // up to MAX_CONTACTS contacts per box-box
        int numc = OdeHelper.collide(o1, o2, contactNum, contacts.getGeomBuffer());
        for (int i = 0; i <= numc; i++) {
            DContact contact = contacts.get(i);
            if (b1 != null && b1.getAttachedPart().PART_TYPE == AbstractPart.partTypes.WHEEL) {
                contact.surface.mode = dContactMu2 | dContactBounce | dContactRolling | dContactApprox1 | dContactFDir1;
                DVector3 vf = new DVector3(0, 0, 1);
                b1.vectorToWorld(new DVector3(1, 0, 0), vf);
                vf.cross(contact.geom.normal);
                dxSafeNormalize3(vf);
                contact.fdir1.set(vf);
                contact.surface.mu = 0.5;//侧向滑动摩擦系数
                contact.surface.mu2 = 2;//前向滑动摩擦系数
                contact.surface.rho = 0.01;//前向滚动摩擦系数
                contact.surface.rho2 = 0.01;
                contact.surface.rhoN = 0.01;
                contact.surface.bounce = 0.0001;
                contact.surface.bounce_vel = 0.1;

            } else if (b2 != null && b2.getAttachedPart().PART_TYPE == AbstractPart.partTypes.WHEEL) {
                contact.surface.mode = dContactMu2 | dContactBounce | dContactRolling | dContactApprox1 | dContactFDir1;
                DVector3 vf = new DVector3(0, 0, 1);
                b2.vectorToWorld(new DVector3(1, 0, 0), vf);
                vf.cross(contact.geom.normal);
                dxSafeNormalize3(vf);
                contact.fdir1.set(vf);
                contact.surface.mu = 0.5;//侧向滑动摩擦系数
                contact.surface.mu2 = 2;//前向滑动摩擦系数
                contact.surface.rho = 0.01;//前向滚动摩擦系数
                contact.surface.rho2 = 0.01;
                contact.surface.rhoN = 0.01;
                contact.surface.bounce = 0.0001;
                contact.surface.bounce_vel = 0.1;

            } else {
                contact.surface.mode = dContactBounce | dContactRolling;
                contact.surface.mu = 500;
                contact.surface.rho = 1;
                contact.surface.bounce = 0.0001;
                contact.surface.bounce_vel = 0.1;
            }
        }
        if (numc != 0) {
            for (int i = 0; i < numc; i++) {
                DJoint c = OdeHelper.createContactJoint(world, contactGroup, contacts.get(i));
                c.attach(b1, b2);
            }
        }
    }
}
