package hungteen.imm.common.world.structure.structures;

import hungteen.imm.common.block.IMMPoiTypes;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 浮屠塔用 Jigsaw 实现难点较多，比如无法解决旋转问题。<br>
 * 故而暂时改成了使用少量硬编码搭配结构方块加载的方式实现。<br>
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/24 14:51
 **/
public class SpiritPagoda {

    public static final int BASE_HEIGHT = 77;
    public static final int LEVEL_COUNT = 7;

    /**
     * 通过检查七个锚点的存在情况来判断是否已经生成了浮屠塔。
     * @return 是否已经生成了浮屠塔。
     */
    private static boolean hasFullPagoda(ServerLevel level, BlockPos center) {
        return getAnchors(level, center).size() == LEVEL_COUNT;
    }

    /**
     * 获取浮屠塔的锚点。
     * @return 锚点列表，按照从下到上的顺序排序。
     */
    public static List<BlockPos> getAnchors(ServerLevel level, BlockPos center) {
        List<PoiRecord> list = level.getPoiManager().getInChunk(IMMPoiTypes.SPIRIT_ANCHOR.holder()::equals, new ChunkPos(center), PoiManager.Occupancy.ANY).toList();
        return level.getPoiManager().getInChunk(IMMPoiTypes.SPIRIT_ANCHOR.holder()::equals, new ChunkPos(center), PoiManager.Occupancy.ANY)
                .map(PoiRecord::getPos)
                .sorted(Comparator.comparingInt(Vec3i::getY))
                .toList();
    }

    /**
     * 在指定位置生成浮屠塔。
     * @param level 在哪个世界生成。
     * @param center 浮屠塔第一层的中心位置。
     */
    public static List<BlockPos> placeSpiritPagoda(ServerLevel level, BlockPos center) {
        BlockPos pos = new BlockPos(center.getX(), BASE_HEIGHT, center.getZ());
        if(! hasFullPagoda(level, pos)) {
            int yOffset = 0;
            for (SpiritPagoda.PagodaPart part : SpiritPagoda.getParts()) {
                for (SpiritPagoda.PartType type : part.getTypes()) {
                    BlockPos offset = part.getOffset(type);
                    placeStructure(level, part.getTemplate(type), pos.offset(offset.getX(), offset.getY() + yOffset, offset.getZ()));
                }
                yOffset += part.height();
            }
        }
        return getAnchors(level, pos);
    }

    private static void placeStructure(ServerLevel level, ResourceLocation structureName, BlockPos pos) {
        StructureTemplate structureTemplate = getStructureTemplate(level, structureName);
        if (structureTemplate != null) {
            StructurePlaceSettings settings = new StructurePlaceSettings();

            structureTemplate.placeInWorld(level, pos, pos, settings, level.random, 3);
        }
    }

    @Nullable
    public static StructureTemplate getStructureTemplate(ServerLevel level, ResourceLocation structureName) {
        return structureName == null ? null : level.getStructureManager().get(structureName).orElse(null);
    }


    private static void checkLoaded(ServerLevel level, ChunkPos start, ChunkPos end) {
        ChunkPos.rangeClosed(start, end).forEach(pos -> {
            level.getChunkSource().getChunk(pos.x, pos.z, true);
        });
        if (ChunkPos.rangeClosed(start, end).anyMatch(pos -> !level.isLoaded(pos.getWorldPosition()))) {
            throw new RuntimeException("Cannot place structure " + " because the chunk is not loaded.");
        }
    }

    public static List<PagodaPart> getParts(){
        List<PagodaPart> parts = new ArrayList<>();
        parts.add(new PagodaPart(1, true, 21, 7, 0));
        parts.add(new PagodaPart(2, true, 27, 8, 0));
        parts.add(new PagodaPart(3, true, 35, 10, 0));
        parts.add(new PagodaPart(4, true, 43, 12, 0));
        parts.add(new PagodaPart(5, false, 51, 14, 32));
        parts.add(new PagodaPart(6, false, 61, 17, 32));
        parts.add(new PagodaPart(7, false, 71, 23, 48));
        return parts;
    }

    /**
     * 浮屠塔的部件信息。
     * @param level 浮屠塔的层数。
     * @param single 当前层是不是只有一个结构方块。
     * @param length 浮屠塔当前俯视角八边形的长度。
     * @param height 浮屠塔当前的层高。
     * @param singleCoverSize 一个单元的覆盖面积。
     */
    public record PagodaPart(int level, boolean single, int length, int height, int singleCoverSize){

        public PartType[] getTypes(){
            if(single()){
                return new PartType[]{PartType.MAIN};
            } else {
                return PartType.values();
            }
        }

        public BlockPos getOffset(PartType type){
            BlockPos offset = new BlockPos(-(length() >> 1), 0, -(length() >> 1));
            switch (type){
                case LEFT -> offset = offset.offset(singleCoverSize(), 0, 0);
                case RIGHT -> offset = offset.offset(0, 0, singleCoverSize());
                case DIAGON -> offset = offset.offset(singleCoverSize(), 0, singleCoverSize());
            }
            return offset;
        }

        public ResourceLocation getTemplate(PartType type){
            if(single()){
                return Util.prefix("spirit_pagoda/spirit_pagoda_level_" + level());
            } else {
                return Util.prefix("spirit_pagoda/spirit_pagoda_level_" + level() + "_" + type.partId);
            }
        }
    }

    public enum PartType {

        MAIN(1),

        LEFT(2),

        RIGHT(3),

        DIAGON(4),

        ;

        private final int partId;

        PartType(int partId) {
            this.partId = partId;
        }
    }

}
