package hungteen.imm.common.entity.human.cultivator;

import hungteen.htlib.util.WeightedList;
import hungteen.imm.common.entity.IMMDataSerializers;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-22 22:16
 **/
public abstract class Cultivator extends HumanLikeEntity {

    private static final EntityDataAccessor<CultivatorType> CULTIVATOR_TYPE = SynchedEntityData.defineId(Cultivator.class, IMMDataSerializers.CULTIVATOR_TYPE.get());

    public Cultivator(EntityType<? extends HumanLikeEntity> type, Level level) {
        super(type, level);
        this.xpReward = 10;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CULTIVATOR_TYPE, CultivatorType.SLIM_STEVE);
    }

    @Override
    public void serverFinalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType) {
        super.serverFinalizeSpawn(accessor, difficultyInstance, spawnType);
        // 随机选择散修类型。
        WeightedList.create(CultivatorType.values()).getRandomItem(accessor.getRandom()).ifPresent(this::setCultivatorType);
        this.setCustomName(this.getCultivatorType().getDisplayName());
        if(! getCultivatorType().isCommon()){
            this.modifyAttributes(getCultivatorType());
        }
    }

    /**
     * 对于特殊的散修类型，可以修改一些数值。
     */
    public void modifyAttributes(CultivatorType type){

    }

    @Override
    public Component getName() {
        return this.getCultivatorType().getDisplayName();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        NBTUtil.read(tag, CultivatorType.CODEC, "CultivatorType", this::setCultivatorType);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        NBTUtil.write(tag, CultivatorType.CODEC, "CultivatorType", this.getCultivatorType());
    }

    public void setCultivatorType(CultivatorType skin){
        entityData.set(CULTIVATOR_TYPE, skin);
    }

    public CultivatorType getCultivatorType(){
        return entityData.get(CULTIVATOR_TYPE);
    }

    public boolean isSlim() {
        return this.getCultivatorType().isSlim();
    }

}
