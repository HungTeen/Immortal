package hungteen.imm.common.entity;

import hungteen.imm.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-21 18:43
 **/
public abstract class IMMGrowableCreature extends IMMCreature {

    private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(IMMGrowableCreature.class, EntityDataSerializers.INT);
    protected int growTick = 0;
    protected int forcedAgeTimer;

    public IMMGrowableCreature(EntityType<? extends IMMGrowableCreature> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(AGE, 1);
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
        if (this.level.isClientSide) {
            if (this.forcedAgeTimer > 0) {
                if (this.forcedAgeTimer % 4 == 0) {
                    this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
                }
                --this.forcedAgeTimer;
            }
        } else{
            if(EntityUtil.isEntityValid(this)){
                if(this.canGrow()){
                    if(this.growTick >= this.getGrowNeedTime()){
                        this.onGrow();
                    }
                    ++ this.growTick;
                }
            }
        }
    }

    public void onGrow(){
        this.setAge(this.getAge() + 1);
        this.growTick = 0;
    }

    public int getMaxAge(){
        return 1;
    }

    public boolean canGrow(){
        return this.getAge() < this.getMaxAge();
    }

    public int getGrowNeedTime(){
        return 24000;
    }

    public boolean spawnEggMatch(ItemStack stack){
        return stack.getItem() instanceof SpawnEggItem item && this.getType().equals(item.getType(stack.getOrCreateTag()));
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
