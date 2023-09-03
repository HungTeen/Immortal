package hungteen.imm.common.entity.creature.monster;

import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.registry.BiomeHelper;
import hungteen.imm.api.records.Spell;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.entity.ai.goal.UseSpellGoal;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.world.levelgen.IMMBiomes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

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

    private static final Map<ResourceKey<Biome>, Supplier<BlockState>> STAKE_MAP = new HashMap<>();
    private static final EntityDataAccessor<BlockState> STAKE = SynchedEntityData.defineId(SharpStake.class, EntityDataSerializers.BLOCK_STATE);

    static {
        STAKE_MAP.putAll(Map.of(
                IMMBiomes.BIRCH_FOREST, Blocks.BIRCH_LOG::defaultBlockState,
                IMMBiomes.CUT_BIRCH_FOREST, Blocks.BIRCH_LOG::defaultBlockState
        ));
    }

    public static void updateStakeMap(ResourceKey<Biome> biome, Supplier<BlockState> state) {
        STAKE_MAP.put(biome, state);
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
        this.goalSelector.addGoal(2, new UseSpellGoal(this));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        if(! accessor.isClientSide()){
            BiomeHelper.get().getResourceKey(accessor.getBiome(blockPosition()).get()).flatMap(key -> JavaHelper.getOpt(STAKE_MAP, key)).ifPresent(sup -> {
                this.setStakeState(sup.get());
            });
            this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_AXE));
        }
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, data, tag);
    }

    @Override
    protected List<Spell> createLearnSpells() {
        return List.of(new Spell(SpellTypes.THROW_ITEM));
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
        return SoundEvents.WOOD_BREAK;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WOOD_STEP;
    }

}
