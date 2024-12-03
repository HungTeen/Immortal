package hungteen.imm.common.entity.undead;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.common.cultivation.realm.MultiRealm;
import hungteen.imm.common.effect.CorpsePoisonEffect;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.entity.ai.goal.AggressiveAttackGoal;
import hungteen.imm.common.entity.ai.goal.UseSpellGoal;
import hungteen.imm.util.ItemUtil;
import hungteen.imm.util.NBTUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Modify from {@link net.minecraft.world.entity.monster.Zombie}.
 *
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-19 23:04
 **/
public class QiZombie extends UndeadEntity {

    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(Util.prefix("baby"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(QiZombie.class, EntityDataSerializers.BOOLEAN);

    public QiZombie(EntityType<? extends UndeadEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return IMMMob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
                ;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BABY_ID, false);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new UseSpellGoal(this));
        this.goalSelector.addGoal(2, new AggressiveAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new FloatGoal(this));
//        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void serverFinalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType) {
        super.serverFinalizeSpawn(accessor, difficultyInstance, spawnType);
        if(accessor.getRandom().nextFloat() < 0.05F){
            this.setBaby(true);
        }
    }

    @Override
    public List<Spell> getRandomSpells(RandomSource random, Element element, RealmType realm) {
        return switch (element){
            case METAL -> List.of(Spell.create(SpellTypes.CRITICAL_HIT));
            default -> List.of();
        };
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        // Pink Zombie.
        if (random.nextFloat() < 0.01F) {
            this.setItemSlot(EquipmentSlot.HEAD, ItemUtil.dyeArmor(Items.LEATHER_HELMET, List.of(DyeColor.PINK)));
            this.setItemSlot(EquipmentSlot.BODY, ItemUtil.dyeArmor(Items.LEATHER_CHESTPLATE, List.of(DyeColor.PINK)));
            this.setItemSlot(EquipmentSlot.LEGS, ItemUtil.dyeArmor(Items.LEATHER_LEGGINGS, List.of(DyeColor.PINK)));
            this.setItemSlot(EquipmentSlot.FEET, ItemUtil.dyeArmor(Items.LEATHER_BOOTS, List.of(DyeColor.PINK)));
        }
        if (random.nextFloat() < (difficulty.getDifficulty() == Difficulty.HARD ? 0.05F : 0.01F)) {
            int i = random.nextInt(3);
            if (i == 0) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean flag = super.doHurtTarget(entity);
        if (flag) {
            // 蔓延火焰。
            float f = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
                entity.igniteForSeconds((float)(2 * (int)f));
            }

            // 附加尸毒。
            if(entity instanceof LivingEntity target && this.hasElement(Element.WOOD)){
                if(this.getRandom().nextFloat() < 0.5F / this.getRoots().size()){
                    CorpsePoisonEffect.attachPoison(target, 1, getRoots().size() == 1 ? 1200 : 600);
                }
            }
        }

        return flag;
    }

    @Override
    public List<MultiRealm> getMultiRealms() {
        return List.of(RealmTypes.LOW_RANK_UNDEAD, RealmTypes.MID_RANK_UNDEAD, RealmTypes.HIGH_RANK_UNDEAD);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_BABY_ID.equals(key)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(key);
    }

    @Override
    protected int getBaseExperienceReward() {
        if (this.isBaby()) {
            this.xpReward = (int)((double)this.xpReward * 2.5);
        }

        return super.getBaseExperienceReward();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        NBTUtil.read(tag, tag::getBoolean, "IsBaby", this::setBaby);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        NBTUtil.write(tag::putBoolean, "IsBaby", this.isBaby());
    }

    @Override
    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    @Override
    public void setBaby(boolean childZombie) {
        this.getEntityData().set(DATA_BABY_ID, childZombie);
        if (!this.level().isClientSide) {
            AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if(attributeinstance != null){
                if (childZombie) {
                    attributeinstance.addOrUpdateTransientModifier(SPEED_MODIFIER_BABY);
                } else {
                    attributeinstance.removeModifier(SPEED_MODIFIER_BABY);
                }
            }
        }
    }

}
