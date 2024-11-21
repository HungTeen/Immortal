package hungteen.imm.common.entity.human.cultivator;

import hungteen.htlib.util.WeightedList;
import hungteen.imm.common.entity.human.HumanEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * 是否碰到敌人直接逃跑
 * 碰到敌人先远战/近战
 * 是否喜欢留后手（藏杀手锏）
 *
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-22 22:16
 **/
public abstract class Cultivator extends HumanEntity {

    private static final EntityDataAccessor<Integer> CULTIVATOR_TYPE = SynchedEntityData.defineId(Cultivator.class, EntityDataSerializers.INT);

    public Cultivator(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
        this.xpReward = 10;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CULTIVATOR_TYPE, CultivatorTypes.SLIM_STEVE.ordinal());
    }

    @Override
    public void serverFinalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType) {
        super.serverFinalizeSpawn(accessor, difficultyInstance, spawnType);
        // 随机选择散修类型。
        WeightedList.create(CultivatorTypes.values()).getRandomItem(accessor.getRandom()).ifPresent(this::setCultivatorType);
        this.setCustomName(this.getCultivatorType().getDisplayName());
        if(! getCultivatorType().isCommon()){
            this.modifyAttributes(getCultivatorType());
        }
    }

    /**
     * 对于特殊的散修类型，可以修改一些数值。
     */
    public void modifyAttributes(CultivatorTypes type){

    }

    @Override
    public Component getName() {
        return this.getCultivatorType().getDisplayName();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("CultivatorType")){
            this.setCultivatorType(CultivatorTypes.valueOf(tag.getString("CultivatorType")));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("CultivatorType", this.getCultivatorType().name());
    }

    public void setCultivatorType(CultivatorTypes skin){
        entityData.set(CULTIVATOR_TYPE, skin.ordinal());
    }

    public CultivatorTypes getCultivatorType(){
        return CultivatorTypes.values()[entityData.get(CULTIVATOR_TYPE)];
    }

    public boolean isSlim() {
        return this.getCultivatorType().isSlim();
    }

}
