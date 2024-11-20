package hungteen.imm.common.entity;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.RandomUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-21 18:43
 **/
public abstract class IMMGrowableMob extends IMMMob {

    private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(IMMGrowableMob.class, EntityDataSerializers.INT);
    protected int growTick = 0;
    protected int forcedAgeTimer;

    public IMMGrowableMob(EntityType<? extends IMMGrowableMob> type, Level level) {
        super(type, level);
        this.refreshDimensions();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(AGE, 1); // [1, max age].
    }

    @Override
    public void serverFinalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType) {
        super.serverFinalizeSpawn(accessor, difficultyInstance, spawnType);
        this.onAgeChangeTo(getRandomSpawnAge().sample(accessor.getRandom()), true);
    }

    public IntProvider getRandomSpawnAge(){
        return UniformInt.of(1, this.getMaxAge());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);
        if(dataAccessor.equals(AGE)) {
            this.refreshDimensions();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.forcedAgeTimer > 0) {
                if (this.forcedAgeTimer % 4 == 0) {
                    this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
                }
                --this.forcedAgeTimer;
            }
        } else{
            if(EntityUtil.isEntityValid(this)){
                if(this.canNaturallyGrow()){
                    if(this.growTick >= this.getGrowNeedTime()){
                        this.onGrow();
                    }
                    ++ this.growTick;
                }
            }
        }
    }

    /**
     * Only on server side.
     */
    public void updateRealmByAge(int age){

    }

    public void onGrow(){
        this.onAgeChangeTo(this.getAge() + 1, false);
        this.growTick = 0;
    }

    public void onAgeChangeTo(int age, boolean firstSpawn){
        if(EntityHelper.isServer(this)){
            this.updateRealmByAge(age);
            if(! firstSpawn){
                this.setRealmStage(RealmStages.PRELIMINARY);
            }
        }
        this.setAge(age);
    }

    public int getMaxAge(){
        return 1;
    }

    public boolean canNaturallyGrow(){
        return this.getAge() < this.getMaxAge();
    }

    public int getGrowNeedTime(){
        return 24000;
    }

    public boolean isMature(){
        return this.getAge() >= this.getMaxAge();
    }

    public boolean spawnEggMatch(ItemStack stack){
        return stack.getItem() instanceof SpawnEggItem item && this.getType().equals(item.getType(stack));
    }

    @Override
    public float getScale() {
        return getAge() * 1F / getMaxAge();
    }

    @Override
    public float getVoicePitch() {
        return RandomUtil.getTriangle(this.random) * 0.2F + (1.5F - 0.5F * getGrowPercent());
    }

    public float getGrowPercent(){
        return getMaxAge() <= 1 ? 1F : (getAge() - 1F) / (getMaxAge() - 1);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("CreatureAge")){
            this.setAge(tag.getInt("CreatureAge"));
        }
        if(tag.contains("CreatureGrowTick")){
            this.growTick = tag.getInt("CreatureGrowTick");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("CreatureAge", this.getAge());
        tag.putInt("CreatureGrowTick", this.growTick);
    }

    public void setAge(int age) {
        entityData.set(AGE, age);
    }

    public int getAge() {
        return entityData.get(AGE);
    }

}
