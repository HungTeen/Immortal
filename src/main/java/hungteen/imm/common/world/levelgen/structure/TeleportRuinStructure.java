package hungteen.imm.common.world.levelgen.structure;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.block.artifacts.TeleportAnchorBlock;
import hungteen.imm.common.world.levelgen.IMMStructurePieces;
import hungteen.imm.common.world.levelgen.IMMStructureTypes;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.Map;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/15 15:04
 */
public class TeleportRuinStructure extends Structure {

    public static final Codec<TeleportRuinStructure> CODEC = simpleCodec(TeleportRuinStructure::new);
    static final ResourceLocation MUD_HOUSE = Util.prefix("teleport_ruins/mud_house");
    private static final ResourceLocation PASSAGE = Util.prefix("teleport_ruins/passage");
    private static final ResourceLocation TELEPORT_RUIN = Util.prefix("teleport_ruins/teleport_ruin");
    private static final int PASSAGE_LEN = 10;
    private static final int LOWEST_HEIGHT = -40;
    static final Map<ResourceLocation, BlockPos> PIVOTS = ImmutableMap.of(MUD_HOUSE, new BlockPos(4, 2, 4), PASSAGE, new BlockPos(1, 5, 1), TELEPORT_RUIN, new BlockPos(4, 2, 7));
    static final Map<ResourceLocation, BlockPos> OFFSETS = ImmutableMap.of(MUD_HOUSE, BlockPos.ZERO, PASSAGE, new BlockPos(7, 0, 6), TELEPORT_RUIN, new BlockPos(0, -3, -2));

    public TeleportRuinStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (builder) -> this.generatePieces(builder, context));
    }

    private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        final ChunkPos chunkpos = context.chunkPos();
        final WorldgenRandom worldgenrandom = context.random();
        final BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), 90, chunkpos.getMinBlockZ());
        final Rotation rotation = Rotation.getRandom(worldgenrandom);
        addPieces(context.structureTemplateManager(), blockpos, rotation, builder, worldgenrandom);
    }

    public static void addPieces(StructureTemplateManager manager, BlockPos pos, Rotation rotation, StructurePieceAccessor accessor, RandomSource randomSource) {
        if (randomSource.nextDouble() < 0.5D) {
            int i = randomSource.nextInt(8) + 4;
            accessor.addPiece(new Piece(manager, TELEPORT_RUIN, pos, rotation, i * 3));

            for(int j = 0; j < i - 1; ++j) {
                accessor.addPiece(new Piece(manager, PASSAGE, pos, rotation, j * 3));
            }
        }
        final int passageCnt = Math.max(RandomHelper.getMinMax(randomSource, pos.getY() / PASSAGE_LEN, (pos.getY() - LOWEST_HEIGHT) / PASSAGE_LEN), 0);
        accessor.addPiece(new Piece(manager, MUD_HOUSE, pos, rotation, 0));
        for(int i = 0; i < passageCnt; ++ i){
            accessor.addPiece(new Piece(manager, PASSAGE, pos, rotation, (i + 1) * PASSAGE_LEN));
        }
        accessor.addPiece(new Piece(manager, TELEPORT_RUIN, pos, rotation, passageCnt * PASSAGE_LEN + 6));
    }

    @Override
    public StructureType<?> type() {
        return IMMStructureTypes.TELEPORT_RUIN.get();
    }

    public static class Piece extends HTTemplateStructurePiece {

        public Piece(StructureTemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation, int below) {
            super(IMMStructurePieces.TELEPORT_RUIN.get(), 0, manager, location, location.toString(), makeSettings(rotation, location), makePosition(location, pos, below));
        }

        public Piece(StructureTemplateManager manager, CompoundTag tag) {
            super(IMMStructurePieces.TELEPORT_RUIN.get(), manager, tag, location -> {
                return makeSettings(Rotation.valueOf(tag.getString("Rot")), location);
            });
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation, ResourceLocation location) {
            return (new StructurePlaceSettings())
                    .setRotation(rotation)
                    .setMirror(Mirror.NONE)
                    .setRotationPivot(PIVOTS.get(location))
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        }

        private static BlockPos makePosition(ResourceLocation location, BlockPos pos, int below) {
            return pos.offset(OFFSETS.get(location)).below(below);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
            super.addAdditionalSaveData(context, tag);
            tag.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
            super.postProcess(level, manager, generator, randomSource, boundingBox, chunkPos, blockPos);
        }

        @Override
        protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor accessor, RandomSource randomSource, BoundingBox boundingBox) {
            if(label.equals("teleport_anchor")){
                accessor.setBlock(pos, IMMBlocks.TELEPORT_ANCHOR.get().defaultBlockState().setValue(TeleportAnchorBlock.CHARGE, randomSource.nextInt(1)), 3);
            } else if(label.equals("chest")){
                BlockEntity blockentity = accessor.getBlockEntity(pos);
                if (blockentity instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)blockentity).setLootTable(BuiltInLootTables.IGLOO_CHEST, randomSource.nextLong());
                }
            }
        }
    }

}
