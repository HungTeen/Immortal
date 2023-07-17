package hungteen.imm.common.entity.creature.monster;

import hungteen.htlib.util.helper.CodecHelper;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.entity.IMMCreature;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/6 14:19
 */
public class SharpStake extends IMMCreature {

    private static final EntityDataAccessor<BlockState> STAKE = SynchedEntityData.defineId(SharpStake.class, EntityDataSerializers.BLOCK_STATE);
    public SharpStake(EntityType<? extends IMMCreature> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STAKE, Blocks.OAK_LOG.defaultBlockState());
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        if(! accessor.isClientSide()){
            // TODO Change texture with biome.
        }
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, data, tag);
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
    public ICultivationType getCultivationType() {
        return CultivationTypes.MONSTER;
    }

    @Override
    public Collection<ISpiritualType> getSpiritualTypes() {
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
