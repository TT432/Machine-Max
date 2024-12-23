package io.github.tt432.machinemax.common.phys;

import io.github.tt432.machinemax.mixin_interface.IMixinLevel;
import org.ode4j.ode.DTriMesh;
import org.ode4j.ode.DTriMeshData;
import org.ode4j.ode.OdeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.ArrayList;
import java.util.List;

public class TerrainBuilder {
//
//    public static void build(ChunkAccess chunk) {
//        IMixinLevel level = (IMixinLevel) chunk.getLevel();
//        AbstractPhysThread thread;
//        DTriMeshData data = OdeHelper.createTriMeshData();
//        List<Integer> indices = new ArrayList<>();
//        List<Float> vertices = new ArrayList<>();
//        BlockPos chunkPos = new BlockPos(chunk.getPos().getMinBlockX(), 0, chunk.getPos().getMinBlockX());
//        if (level != null) thread = level.machine_Max$getPhysThread();
//        else return;
//        //遍历区块方块
//        for (int y = chunk.getMinBuildHeight(); y <= getHighestBlockY(chunk); y++) {
//            for (int x = 0; x <= 15; x++) {
//                for (int z = 0; z <= 15; z++) {
//                    BlockPos currentPos = new BlockPos(chunkPos.getX() + x, y, chunkPos.getZ() + z);
//                    BlockState blockState = chunk.getBlockState(currentPos);
//                    if (!blockState.isAir()) {
//                        // 检查与空气的相邻方向
//                        Direction[] airDirections = checkNeighborAir(chunk, currentPos);
//                        // 添加顶点和索引
//                        if (airDirections.length > 0)
//                            addFaceVerticesAndIndices(vertices, indices, currentPos, airDirections);
//                    }
//                }
//            }
//        }
//        // 构建三角网格
//        if (!vertices.isEmpty() && !indices.isEmpty()) {
//            //TODO:处理顶点和索引，将多个相邻共面三角形合并为单个三角形
//            //转换为对应类型的 float 数组
//            float[] verticesArray = new float[vertices.size()]; // 创建一个新的 float 数组
//            for (int i = 0; i < vertices.size(); i++)
//                verticesArray[i] = vertices.get(i); // 将 List 中的每个 Float 值添加到数组中
//            // 转换索引列表为 int 数组
//            int[] indicesArray = new int[indices.size()];
//            for (int i = 0; i < indices.size(); i++)
//                indicesArray[i] = indices.get(i); // 将 List 中的每个 Integer 值添加到 int 数组中
//            data.build(verticesArray, indicesArray);
//            data.preprocess();
//            DTriMesh triMesh = OdeHelper.createTriMesh(null, data, null, null);
//            thread.space.geomAddEnQueue(triMesh);
//        }
//    }

    public static Direction[] checkNeighborAir(ChunkAccess chunk, BlockPos pos) {
        List<Direction> airDirections = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            if (chunk.getBlockState(pos.relative(direction)).isAir()) {
                airDirections.add(direction); // 添加空气方向到列表
            }
        }
        // 将方向列表转换为数组并返回
        return airDirections.toArray(new Direction[0]);
    }

    public static int getHighestBlockY(ChunkAccess chunk) {
        int highestSectionIndex = chunk.getHighestFilledSectionIndex();
        if (highestSectionIndex == -1) {
            return chunk.getMinBuildHeight(); // 如果没有填充，返回最小建造高度
        }
        // 将区块段索引转换为 Y 坐标
        return SectionPos.sectionToBlockCoord(highestSectionIndex) + 15; // 加15以获取最高的方块
    }

    private static void addFaceVerticesAndIndices(List<Float> vertices, List<Integer> indices, BlockPos blockPos, Direction[] directions) {
        List<String> addedVertices = new ArrayList<>(); // 用于追踪已添加顶点的字符串格式
        int baseIndex; // 当前四个顶点的基础索引

        for (Direction direction : directions) {
            // 计算四个顶点的三维坐标
            int[] vertexOffsets = getVertexOffsets(direction);
            baseIndex = vertices.size() / 3; // 当前顶点列表的基础索引

            // 添加当前方向的顶点
            for (int i = 0; i < 4; i++) {
                int x = blockPos.getX() + vertexOffsets[i * 3];
                int y = blockPos.getY() + vertexOffsets[i * 3 + 1];
                int z = blockPos.getZ() + vertexOffsets[i * 3 + 2];

                // 确保顶点没有重复添加
                String vertexKey = x + "_" + y + "_" + z; // 组合坐标为唯一键
                if (!addedVertices.contains(vertexKey)) {
                    vertices.add((float) x);
                    vertices.add((float) y);
                    vertices.add((float) z);
                    addedVertices.add(vertexKey); // 添加顶点到追踪列表
                }
            }

            // 重新计算基索引（现在是合理的）
            baseIndex = vertices.size() / 3 - 4; // 确保索引在最后添加的四个顶点之前

            // 添加两个三角面，分别使用四个顶点
            indices.add(baseIndex);     // 第一个三角形的第一个顶点索引
            indices.add(baseIndex + 1); // 第一个三角形的第二个顶点索引
            indices.add(baseIndex + 2); // 第一个三角形的第三个顶点索引

            indices.add(baseIndex);     // 第二个三角形的第一个顶点索引
            indices.add(baseIndex + 2); // 第二个三角形的第二个顶点索引
            indices.add(baseIndex + 3); // 第二个三角形的第三个顶点索引
        }
    }


    private static int[] getVertexOffsets(Direction direction) {
        return switch (direction) {
            case DOWN -> new int[]{
                    0, 0, 0, // 基点
                    1, 0, 0, // 右边
                    1, 0, 1, // 右上
                    0, 0, 1  // 左上
            };
            case UP -> new int[]{
                    0, 1, 0,
                    1, 1, 0,
                    1, 1, 1,
                    0, 1, 1
            };
            case NORTH -> new int[]{
                    0, 0, 0,
                    1, 0, 0,
                    1, 1, 0,
                    0, 1, 0
            };
            case SOUTH -> new int[]{
                    0, 0, 1,
                    1, 0, 1,
                    1, 1, 1,
                    0, 1, 1
            };
            case WEST -> new int[]{
                    0, 0, 0,
                    0, 0, 1,
                    0, 1, 1,
                    0, 1, 0
            };
            case EAST -> new int[]{
                    1, 0, 0,
                    1, 0, 1,
                    1, 1, 1,
                    1, 1, 0
            };
        };
    }

}
