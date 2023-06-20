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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
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
    private static final ResourceLocation TELEPORT_RUIN = Util.prefix("teleport_ruins/teleport_ruin");
    private static final int RUIN_HEIGHT = 5;
    private static final int LOWEST_HEIGHT = -40;
    static final Map<ResourceLocation, BlockPos> PIVOTS = ImmutableMap.of(MUD_HOUSE, new BlockPos(5, 1, 6), TELEPORT_RUIN, new BlockPos(4, RUIN_HEIGHT, 11));
    static final Map<ResourceLocation, BlockPos> OFFSETS = ImmutableMap.of(MUD_HOUSE, BlockPos.ZERO, TELEPORT_RUIN, new BlockPos(1, -RUIN_HEIGHT, -5));

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
        final BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), 0, chunkpos.getMinBlockZ());
        final Rotation rotation = Rotation.getRandom(worldgenrandom);
        addPieces(context.structureTemplateManager(), blockpos, rotation, builder, worldgenrandom);
    }

    public static void addPieces(StructureTemplateManager manager, BlockPos pos, Rotation rotation, StructurePieceAccessor accessor, RandomSource randomSource) {
        final int baseHeight = RandomHelper.getMinMax(randomSource, LOWEST_HEIGHT, - RUIN_HEIGHT - 5);
        accessor.addPiece(new Piece(manager, MUD_HOUSE, pos, rotation, baseHeight));
        accessor.addPiece(new Piece(manager, TELEPORT_RUIN, pos, rotation, baseHeight));
    }

    @Override
    public StructureType<?> type() {
        return IMMStructureTypes.TELEPORT_RUIN.get();
    }

    public static class Piece extends HTTemplateStructurePiece {

        private final int baseHeight;

        public Piece(StructureTemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation, int baseHeight) {
            super(IMMStructurePieces.TELEPORT_RUIN.get(), 0, manager, location, location.toString(), makeSettings(rotation, location), makePosition(location, pos));
            this.baseHeight = baseHeight;
        }

        public Piece(StructureTemplateManager manager, CompoundTag tag) {
            super(IMMStructurePieces.TELEPORT_RUIN.get(), manager, tag, location -> {
                return makeSettings(Rotation.valueOf(tag.getString("Rot")), location);
            });
            this.baseHeight = tag.getInt("BaseHeight");
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation, ResourceLocation location) {
            return (new StructurePlaceSettings())
                    .setRotation(rotation)
                    .setMirror(Mirror.NONE)
                    .setRotationPivot(PIVOTS.get(location))
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        private static BlockPos makePosition(ResourceLocation location, BlockPos pos) {
            return pos.offset(OFFSETS.get(location));
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
            super.addAdditionalSaveData(context, tag);
            tag.putString("Rot", this.placeSettings.getRotation().name());
            tag.putInt("BaseHeight", this.baseHeight);
        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
            final ResourceLocation location = new ResourceLocation(this.templateName);
            final StructurePlaceSettings settings = makeSettings(this.placeSettings.getRotation(), location);
            if(location.equals(MUD_HOUSE)){
                final BlockPos blockpos = OFFSETS.get(location);
                final BlockPos pos = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(settings, new BlockPos(3 - blockpos.getX(), 0, -blockpos.getZ())));
                final int height = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
                this.templatePosition = this.templatePosition.offset(0, height - 1, 0);
            } else {
                this.templatePosition = new BlockPos(this.templatePosition.getX(), this.baseHeight, this.templatePosition.getZ());
            }
            super.postProcess(level, manager, generator, randomSource, boundingBox, chunkPos, blockPos);
            if(location.equals(MUD_HOUSE)){
                final int topY = this.templatePosition.getY();
                BlockPos pos = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(settings, new BlockPos(5, 1, 6)));
                for(int h = this.baseHeight + RUIN_HEIGHT; h <= topY; ++ h){
                    for(int x = -1; x <= 1; ++ x){
                        for(int z = -1; z <= 1; ++ z){
                            final Block block = (x == 0 && z == 0) ? Blocks.AIR : Blocks.DEEPSLATE_TILES;
                            level.setBlock(new BlockPos(pos.getX() + x, h, pos.getZ() + z), block.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }

        @Override
        protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor accessor, RandomSource randomSource, BoundingBox boundingBox) {
            if(label.equals("teleport_anchor")){
                accessor.setBlock(pos, IMMBlocks.TELEPORT_ANCHOR.get().defaultBlockState().setValue(TeleportAnchorBlock.CHARGE, randomSource.nextInt(1)), 3);
            } else if(label.equals("chest")){
                accessor.setBlock(pos, Blocks.CHEST.defaultBlockState(), 3);
                BlockEntity blockentity = accessor.getBlockEntity(pos);
                if (blockentity instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)blockentity).setLootTable(BuiltInLootTables.IGLOO_CHEST, randomSource.nextLong());
                }
            }
        }
    }

}
