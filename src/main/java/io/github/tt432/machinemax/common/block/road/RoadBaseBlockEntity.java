package io.github.tt432.machinemax.common.block.road;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.block.MMBlockEntities;
import io.github.tt432.machinemax.mixin_interface.IMixinBlockState;
import io.github.tt432.machinemax.mixin_interface.IMixinLevel;
import io.github.tt432.machinemax.util.physics.math.DVector3;
import io.github.tt432.machinemax.util.physics.ode.DHeightfieldData;
import io.github.tt432.machinemax.util.physics.ode.OdeHelper;
import io.github.tt432.machinemax.util.physics.ode.internal.DxTrimeshHeightfield;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class RoadBaseBlockEntity extends BlockEntity {

    protected double width = 3;//道路节点宽度
    protected double pitch = 0;//道路节点坡度(角度,在±80度之间)
    protected double roll = 0;//道路节点倾斜角(角度，在±80度之间)
    double[] heightData = new double[17 * 17];//道路高程数据
    DHeightfieldData heightfieldData = OdeHelper.createHeightfieldData();//道路高程图数据
    DxTrimeshHeightfield heightfield;//道路几何形状

    public RoadBaseBlockEntity(BlockPos pos, BlockState blockState) {
        super(MMBlockEntities.ROAD_BASE_BLOCK_ENTITY.get(), pos, blockState);
        for(int i = 0; i < 17; i++){
            for(int j = 0; j < 17; j++){
                heightData[i*17+j] = (double) j /17/2;
            }
        }
    }
    public static void tick(Level level, BlockPos blockPos, BlockState blockState, RoadBaseBlockEntity blockEntity) {
        //TODO:遍历范围内方块，逐一抬升。此方法不行，另想办法
//        Vec3 pos = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
//        for(int i = 0; i < 17; i++){
//            for(int j = 0; j < 17; j++){
//                BlockState state = level.getBlockState(new BlockPos(blockPos.getX()+i-8, blockPos.getY()+2, blockPos.getZ()+j-8));
//                ((IMixinBlockState)state).machine_Max$setRoadOffset(new Vec3(0,blockEntity.heightData[i*17+j],0));
//            }
//        }
    }
    @Override
    public void onLoad() {
        super.onLoad();
        //根据连接的道路节点信息，创建道路高程图
        heightfieldData.build(heightData, false,
                16, 16,
                3, 3,
                1, 0, 0, false);
        heightfield = new DxTrimeshHeightfield(null, heightfieldData, true, true);
        BlockPos pos = this.getBlockPos();
        heightfield.setPosition(pos.getX(), pos.getY()+2, pos.getZ());//挪到正确的位置
        //将道路几何体加入物理世界
        loadHeightfield();
    }

    public void loadHeightfield() {
        ((IMixinLevel) Objects.requireNonNull(getLevel())).machine_Max$getPhysThread().space.geomAddEnQueue(heightfield);//卸载区块时从物理世界移除道路碰撞体
    }

    public void unloadHeightfield() {
        //卸载高度图时清除道路碰撞体，释放内存
        ((IMixinLevel) Objects.requireNonNull(getLevel())).machine_Max$getPhysThread().space.geomRemoveEnQueue(heightfield);//卸载区块时从物理世界移除道路碰撞体
        MachineMax.LOGGER.info("RoadBaseBlockEntity unloadHeightfield");
    }

    @Override
    public void onChunkUnloaded() {
        //卸载区块时清除道路碰撞体，释放内存
        unloadHeightfield();
        super.onChunkUnloaded();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        width = tag.getDouble("width");
        pitch = tag.getDouble("pitch");
        roll = tag.getDouble("roll");
        super.loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putDouble("width", width);
        tag.putDouble("pitch", pitch);
        tag.putDouble("roll", roll);
    }

}
