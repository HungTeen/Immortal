package hungteen.imm.common.entity.creature.monster;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.SimpleWeightedList;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.records.Spell;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.entity.ai.goal.LookAtTargetGoal;
import hungteen.imm.common.entity.ai.goal.UseSpellGoal;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.tag.IMMBiomeTags;
import hungteen.imm.common.world.biome.IMMBiomes;
import hungteen.imm.util.MathUtil;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/6 14:19
 */
public class SharpStake extends IMMMob {

    private static final Map<TagKey<Biome>, Supplier<BlockState>> STAKE_MAP = new HashMap<>();
    private static final List<Pair<Supplier<ItemStack>, Integer>> AXES = new ArrayList<>();
    private static final EntityDataAccessor<BlockState> STAKE = SynchedEntityData.defineId(SharpStake.class, EntityDataSerializers.BLOCK_STATE);

    static {
        STAKE_MAP.putAll(Map.of(
                IMMBiomeTags.HAS_BIRCH_TREE, Blocks.BIRCH_LOG::defaultBlockState,
                IMMBiomeTags.HAS_DARK_OAK_TREE, Blocks.DARK_OAK_LOG::defaultBlockState,
                IMMBiomeTags.HAS_JUNGLE_TREE, Blocks.JUNGLE_LOG::defaultBlockState,
                IMMBiomeTags.HAS_ACACIA_TREE, Blocks.ACACIA_LOG::defaultBlockState,
                IMMBiomeTags.HAS_SPRUCE_TREE, Blocks.SPRUCE_LOG::defaultBlockState,
                IMMBiomeTags.HAS_CHERRY_TREE, Blocks.CHERRY_LOG::defaultBlockState
        ));
        AXES.addAll(List.of(
                Pair.of(() -> new ItemStack(Items.WOODEN_AXE), 10),
                Pair.of(() -> new ItemStack(Items.STONE_AXE), 10),
                Pair.of(() -> new ItemStack(Items.IRON_AXE), 15),
                Pair.of(() -> new ItemStack(Items.DIAMOND_AXE), 8),
                Pair.of(() -> new ItemStack(Items.IRON_SWORD), 10)
        ));
    }

    public static void updateStakeMap(TagKey<Biome> biomeTag, Supplier<BlockState> state) {
        STAKE_MAP.put(biomeTag, state);
    }

    public static void addAxe(Supplier<ItemStack> axe, int weight) {
        AXES.add(Pair.of(axe, weight));
    }

    public SharpStake(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.MOVEMENT_SPEED, 0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STAKE, Blocks.OAK_LOG.defaultBlockState());
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new LookAtTargetGoal(this));
        this.goalSelector.addGoal(2, new UseSpellGoal(this));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        if(! accessor.isClientSide()){
            // Choose skin.
            STAKE_MAP.forEach((biomeTag, state) -> {
                if(accessor.getBiome(blockPosition()).is(biomeTag)){
                    this.setStakeState(state.get());
                }
            });
            // Roll axe.
            final SimpleWeightedList.Builder<ItemStack> axes = SimpleWeightedList.builder();
            AXES.forEach(pair -> axes.add(pair.getFirst().get(), pair.getSecond()));
            this.setItemInHand(InteractionHand.MAIN_HAND, axes.build().getItem(accessor.getRandom()).orElse(new ItemStack(Items.GOLDEN_AXE)));
        }
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, data, tag);
    }

    @Override
    protected List<Spell> createLearnSpells() {
        return List.of(new Spell(SpellTypes.THROW_ITEM));
    }

    @Override
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)){
            if(this.getPose() == Pose.CROUCHING){
                this.setPose(Pose.STANDING);
            }
            if(notInCenter(this.getX() - this.blockPosition().getX()) || notInCenter(this.getZ() - this.blockPosition().getZ())){
                this.setPos(this.blockPosition().getX() + 0.5D, this.getY(), this.blockPosition().getZ() + 0.5D);
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if(accessor.equals(DATA_POSE)){
            if(this.getPose() == Pose.CROUCHING){
                ParticleUtil.spawnParticlesOnBlockFaces(this.level(), this.blockPosition(), ParticleUtil.block(this.getStakeState()), UniformInt.of(3, 5), MathUtil.getHorizontalDirections());
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if(super.hurt(source, damage)){
            this.setPose(Pose.CROUCHING);
            return true;
        }
        return false;
    }

    protected boolean notInCenter(double delta){
        return Math.abs(delta) < 0.49 || Math.abs(delta) > 0.51;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("StakeType")){
            CodecHelper.parse(BlockState.CODEC, tag.get("StakeType"))
                    .result().ifPresent(this::setStakeState);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        CodecHelper.encodeNbt(BlockState.CODEC, getStakeState())
                .result().ifPresent(nbt -> tag.put("StakeType", nbt));
    }

    public BlockState getStakeState() {
        return entityData.get(STAKE);
    }

    public void setStakeState(BlockState state) {
        entityData.set(STAKE, state);
    }

    @Override
    protected IRealmType getDefaultRealm() {
        return RealmTypes.MONSTER_LEVEL_1;
    }

    @Override
    public List<ISpiritualType> getSpiritualTypes() {
        return List.of(SpiritualTypes.METAL);
    }

    @Override
    protected @Nullable SoundEvent getSpawnSound() {
        return SoundEvents.WOOD_PLACE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WOOD_HIT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOOD_BREAK;
    }
}
