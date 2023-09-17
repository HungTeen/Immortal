package hungteen.imm.common.world.structure.pools;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.registry.EntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-14 21:34
 **/
public class SpawnEntityPoolElement extends StructurePoolElement {

    public static final Codec<SpawnEntityPoolElement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntityHelper.get().getCodec().fieldOf("type").forGetter(SpawnEntityPoolElement::getEntityType),
            projectionCodec()
    ).apply(instance, SpawnEntityPoolElement::new));

    private final EntityType<?> entityType;
    private final CompoundTag defaultJigsawNBT;

    public SpawnEntityPoolElement(EntityType<?> entityType, StructureTemplatePool.Projection projection) {
        super(projection);
        this.entityType = entityType;
        this.defaultJigsawNBT = this.fillDefaultJigsawNBT();
    }

    private CompoundTag fillDefaultJigsawNBT() {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putString("name", "minecraft:bottom");
        compoundtag.putString("final_state", "minecraft:air");
        compoundtag.putString("pool", "minecraft:empty");
        compoundtag.putString("target", "minecraft:empty");
        compoundtag.putString("joint", JigsawBlockEntity.JointType.ROLLABLE.getSerializedName());
        return compoundtag;
    }

    @Override
    public Vec3i getSize(StructureTemplateManager manager, Rotation rotation) {
        return Vec3i.ZERO;
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager manager, BlockPos pos, Rotation rotation, RandomSource source) {
        final List<StructureTemplate.StructureBlockInfo> list = Lists.newArrayList();
        list.add(new StructureTemplate.StructureBlockInfo(pos, Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, FrontAndTop.fromFrontAndTop(Direction.DOWN, Direction.SOUTH)), this.defaultJigsawNBT));
        return list;
    }

    @Override
    public BoundingBox getBoundingBox(StructureTemplateManager manager, BlockPos pos, Rotation rotation) {
        final Vec3i vec3i = this.getSize(manager, rotation);
        return new BoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + vec3i.getX(), pos.getY() + vec3i.getY(), pos.getZ() + vec3i.getZ());
    }

    @Override
    public boolean place(StructureTemplateManager manager, WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, BlockPos pos1, BlockPos pos2, Rotation rotation, BoundingBox box, RandomSource source, boolean p_227345_) {
        final Entity entity = this.getEntityType().create(level.getLevel());
        if (entity != null) {
            entity.moveTo(pos1, 0.0F, 0.0F);
            if(entity instanceof Mob mob){
                mob.setPersistenceRequired();
                ForgeEventFactory.onFinalizeSpawn(mob, level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.STRUCTURE, null, null);
            }
            level.addFreshEntityWithPassengers(entity);
            level.setBlock(pos1, Blocks.AIR.defaultBlockState(), 2);
            return true;
        }
        return false;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    @Override
    public StructurePoolElementType<?> getType() {
//        return IMMPoolTypes.SPAWN_ENTITY.get();
        return null;
    }

}
