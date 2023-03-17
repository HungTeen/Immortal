package hungteen.immortal.common.entity.human.cultivator;

import hungteen.immortal.common.entity.human.HumanEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * 是否碰到敌人直接逃跑
 * 碰到敌人先远战/近战
 * 是否喜欢留后手（藏杀手锏）
 *
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-22 22:16
 **/
public abstract class Cultivator extends HumanEntity {

    private static final EntityDataAccessor<Integer> CULTIVATOR_TYPE = SynchedEntityData.defineId(Cultivator.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SLIM = SynchedEntityData.defineId(Cultivator.class, EntityDataSerializers.BOOLEAN);

    public Cultivator(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(CULTIVATOR_TYPE, CultivatorTypes.COMMON.ordinal());
        entityData.define(SLIM, false);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag compoundTag) {
        if(!accessor.isClientSide()){
            final int type = accessor.getRandom().nextInt(CultivatorTypes.values().length);
            this.setCultivatorType(CultivatorTypes.values()[type]);
            this.setSlim(accessor.getRandom().nextInt(2) == 0);
            this.setCustomName(this.getCultivatorType().getDisplayName());
        }
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, groupData, compoundTag);
    }

    @Override
    public Component getName() {
        return this.getCultivatorType() == CultivatorTypes.COMMON ? super.getName() : this.getCultivatorType().getDisplayName();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("CultivatorType")){
            this.setCultivatorType(CultivatorTypes.values()[tag.getInt("CultivatorType")]);
        }
        if(tag.contains("CultivatorSlim")){
            this.setSlim(tag.getBoolean("CultivatorSlim"));
        }
        if(tag.contains("CultivatorRoots")){
            setRootTag(tag.getCompound("CultivatorRoots"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("CultivatorType", this.getCultivatorType().ordinal());
        tag.putBoolean("CultivatorSlim", this.isSlim());
        tag.put("CultivatorRoots", this.getRootTag());
    }

    public void setCultivatorType(CultivatorTypes skin){
        entityData.set(CULTIVATOR_TYPE, skin.ordinal());
    }

    public CultivatorTypes getCultivatorType(){
        return CultivatorTypes.values()[entityData.get(CULTIVATOR_TYPE)];
    }

    public void setSlim(boolean slim) {
        entityData.set(SLIM, slim);
    }

    public boolean isSlim() {
        return entityData.get(SLIM);
    }

}
