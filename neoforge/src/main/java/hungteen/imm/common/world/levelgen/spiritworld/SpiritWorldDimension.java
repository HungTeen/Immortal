package hungteen.imm.common.world.levelgen.spiritworld;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.imm.common.world.data.SpiritRegionData;
import hungteen.imm.common.world.levelgen.IMMLevels;
import hungteen.imm.common.world.structure.structures.SpiritPagoda;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/24 15:11
 **/
public class SpiritWorldDimension {

    public static void teleportToSpiritRegion(ServerLevel level, Player player) {
        if (!level.dimension().equals(IMMLevels.SPIRIT_WORLD)) {
            Vec3 spiritRegionPos = SpiritRegionData.getPlayerSpiritRegionPosition(level, player);
            Vec3 centerPos = MathHelper.toVec3(MathHelper.toBlockPos(spiritRegionPos));
            ServerLevel spiritWorld = level.getServer().getLevel(IMMLevels.SPIRIT_WORLD);
            if (spiritWorld != null) {
                PlayerUtil.setData(player, data -> {
                    data.getMiscData().setLastPosBeforeSpiritWorld(level.dimension(), player.position());
                });
                player.changeDimension(new DimensionTransition(spiritWorld, centerPos, Vec3.ZERO, 0, 0, false, new EnterSpiritRegionTransition()));
                BlockPos pos = MathHelper.toBlockPos(spiritRegionPos);
                placeSpiritPagoda(spiritWorld, pos);
            }
        }
    }

    public static void placeSpiritPagoda(ServerLevel level, BlockPos center) {
        int yOffset = 0;
        for (SpiritPagoda.PagodaPart part : SpiritPagoda.getParts()) {
            for (SpiritPagoda.PartType type : part.getTypes()) {
                BlockPos offset = part.getOffset(type);
                placeStructure(level, part.getTemplate(type), center.offset(offset.getX(), offset.getY() + yOffset, offset.getZ()));
            }
            yOffset += part.height();
        }
    }

    private static void placeStructure(ServerLevel level, ResourceLocation structureName, BlockPos center) {
        StructureTemplate structureTemplate = getStructureTemplate(level, structureName);
        if (structureTemplate != null) {
            StructurePlaceSettings settings = new StructurePlaceSettings();

            structureTemplate.placeInWorld(level, center, center, settings, level.random, 3);
        }
    }

    @Nullable
    private static StructureTemplate getStructureTemplate(ServerLevel level, ResourceLocation structureName) {
        return structureName == null ? null : level.getStructureManager().get(structureName).orElse(null);
    }

//    public static void placeSpiritPagoda(ServerLevel level, Player player, BlockPos pos) {
//        Holder<Structure> holder = level.registryAccess().holderOrThrow(IMMStructures.SPIRIT_PAGODA);
//        DummyJigsawStructure structure = new DummyJigsawStructure(holder, level, pos);
//        StructureStart structurestart = structure.generate(1);
//        if (!structurestart.isValid()) {
//
//        } else {
//            BoundingBox boundingbox = structurestart.getBoundingBox();
//            ChunkPos chunkpos = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()), SectionPos.blockToSectionCoord(boundingbox.minZ()));
//            ChunkPos chunkpos1 = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()), SectionPos.blockToSectionCoord(boundingbox.maxZ()));
//            checkLoaded(level, chunkpos, chunkpos1);
//            ChunkPos.rangeClosed(chunkpos, chunkpos1)
//                    .forEach(
//                            chunkPos -> structurestart.placeInChunk(
//                                    level,
//                                    level.structureManager(),
//                                    level.getChunkSource().getGenerator(),
//                                    level.getRandom(),
//                                    new BoundingBox(
//                                            chunkPos.getMinBlockX(),
//                                            level.getMinBuildHeight(),
//                                            chunkPos.getMinBlockZ(),
//                                            chunkPos.getMaxBlockX(),
//                                            level.getMaxBuildHeight(),
//                                            chunkPos.getMaxBlockZ()
//                                    ),
//                                    chunkPos
//                            )
//                    );
//        }
//    }

    private static void checkLoaded(ServerLevel level, ChunkPos start, ChunkPos end) {
        ChunkPos.rangeClosed(start, end).forEach(pos -> {
            level.getChunkSource().getChunk(pos.x, pos.z, true);
        });
        if (ChunkPos.rangeClosed(start, end).anyMatch(pos -> !level.isLoaded(pos.getWorldPosition()))) {
            throw new RuntimeException("Cannot place structure " + " because the chunk is not loaded.");
        }
    }

    public static class EnterSpiritRegionTransition implements DimensionTransition.PostDimensionTransition {

        @Override
        public void onTransition(Entity entity) {
            if (entity.level() instanceof ServerLevel serverLevel && entity instanceof Player player) {
                Vec3 position = SpiritRegionData.getPlayerSpiritRegionPosition(serverLevel, player);
//                SpiritRegion spiritRegion = DummyEntityManager.addEntity(serverLevel, new SpiritRegion(serverLevel, position.add(0.5, 0, 0.5), REGION_RADIUS << 1));
//                if (spiritRegion != null) {
//                    spiritRegion.setOwner(player);
//                } else {
//                    throw new RuntimeException("Failed to create spirit region.");
//                }
//                for (BlockPos blockpos : BlockPos.betweenClosed(
//                        (int)position.x() - REGION_RADIUS, REGION_HEIGHT, (int) position.z() - REGION_RADIUS, (int)position.x() + REGION_RADIUS, REGION_HEIGHT, (int) position.z() + REGION_RADIUS
//                )) {
//                    serverLevel.setBlock(blockpos, Blocks.END_STONE.defaultBlockState(), 3);
//                }
                entity.teleportTo(position.x(), position.y() + 1, position.z());
            }
        }
    }

//    public static class DummyJigsawStructure {
//
//        private final Structure.GenerationContext context;
//        private final JigsawStructure structure;
//        private final BlockPos pos;
//
//        public DummyJigsawStructure(Holder<Structure> holder, ServerLevel level, BlockPos pos) {
//            if (holder.value() instanceof JigsawStructure) {
//                this.structure = (JigsawStructure) holder.value();
//            } else {
//                throw new RuntimeException("Structure is not a JigsawStructure.");
//            }
//            this.pos = pos;
//
//            ChunkGenerator generator = level.getChunkSource().getGenerator();
//            this.context = new Structure.GenerationContext(
//                    level.registryAccess(),
//                    generator,
//                    generator.getBiomeSource(),
//                    level.getChunkSource().randomState(),
//                    level.getStructureManager(),
//                    level.getSeed(),
//                    new ChunkPos(pos),
//                    level,
//                    JavaHelper::alwaysTrue
//            );
//        }
//
//        public StructureStart generate(int references) {
//            ChunkPos chunkpos = context.chunkPos();
//
//            Optional<Structure.GenerationStub> optional = JigsawPlacement.addPieces(
//                    context,
//                    structure.startPool,
//                    Optional.empty(),
//                    10,
//                    pos,
//                    false,
//                    Optional.empty(),
//                    100,
//                    PoolAliasLookup.create(List.of(), pos, context.seed()),
//                    JigsawStructure.DEFAULT_DIMENSION_PADDING,
//                    JigsawStructure.DEFAULT_LIQUID_SETTINGS
//            );
//
//            if (optional.isPresent()) {
//                StructurePiecesBuilder structurepiecesbuilder = optional.get().getPiecesBuilder();
//                StructureStart structurestart = new StructureStart(structure, chunkpos, references, structurepiecesbuilder.build());
//                if (structurestart.isValid()) {
//                    return structurestart;
//                }
//            }
//            return StructureStart.INVALID_START;
//        }
//
//    }
}
