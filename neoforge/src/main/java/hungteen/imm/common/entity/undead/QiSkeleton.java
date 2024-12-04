package hungteen.imm.common.entity.undead;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.cultivation.realm.MultiRealm;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.item.artifacts.WoodBowItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Modify from {@link net.minecraft.world.entity.monster.AbstractSkeleton}.
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/4 17:22
 **/
public class QiSkeleton extends UndeadEntity implements RangedAttackMob {

    private final RangedBowAttackGoal<QiSkeleton> bowGoal = new RangedBowAttackGoal<>(this, 1.0, 20, 15.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2, false) {
        @Override
        public void stop() {
            super.stop();
            QiSkeleton.this.setAggressive(false);
        }

        @Override
        public void start() {
            super.start();
            QiSkeleton.this.setAggressive(true);
        }
    };

    public QiSkeleton(EntityType<? extends UndeadEntity> type, Level level) {
        super(type, level);
        this.reassessWeaponGoal();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return IMMMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.26F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                ;
    }

    @Override
    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0));
//        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public void serverFinalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType) {
        super.serverFinalizeSpawn(accessor, difficultyInstance, spawnType);
        if (accessor.getRandom().nextFloat() < 0.04F) {
            this.setBaby(true);
        }
        this.reassessWeaponGoal();
    }

    @Override
    public List<Spell> getRandomSpells(RandomSource random, Element element, RealmType realm) {
        return switch (element) {
//            case FIRE -> List.of(Spell.create(SpellTypes.CRITICAL_HIT));
            default -> List.of();
        };
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(IMMItems.WOOD_BOW.get()));
    }

    /**
     * 根据武器切换攻击方式。
     */
    public void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem));
            if (itemstack.getItem() instanceof BowItem) {
                this.bowGoal.setMinAttackInterval(this.getAttackInterval());
                this.goalSelector.addGoal(4, this.bowGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack weapon = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem));
        ItemStack itemstack1 = this.getProjectile(weapon);
        AbstractArrow abstractarrow = this.getArrow(itemstack1, distanceFactor, weapon);
        if (weapon.getItem() instanceof ProjectileWeaponItem weaponItem){
            abstractarrow = weaponItem.customArrow(abstractarrow, itemstack1, weapon);
        }

        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333) - abstractarrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * 0.2F, d2, 1.8F, (float)(10 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);

        if((this.hasElement(Element.WOOD) && this.getRandom().nextFloat() < 0.5F)
                || (RealmTypes.LOW_RANK_UNDEAD.hasRealm(this.getRealm()) && this.getRandom().nextFloat() < 0.2F)
                || (RealmTypes.MID_RANK_UNDEAD.hasRealm(this.getRealm()) && this.getRandom().nextFloat() < 0.35F)){
            WoodBowItem.attachElementOnArrow(this, abstractarrow, 5, 10);
        }
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeapon) {
        return projectileWeapon instanceof BowItem;
    }

    protected AbstractArrow getArrow(ItemStack arrow, float velocity, @Nullable ItemStack weapon) {
        return ProjectileUtil.getMobArrow(this, arrow, velocity, weapon);
    }

    @Override
    public List<MultiRealm> getMultiRealms() {
        return List.of(RealmTypes.LOW_RANK_UNDEAD, RealmTypes.MID_RANK_UNDEAD);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.reassessWeaponGoal();
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        super.setItemSlot(slot, stack);
        if (!this.level().isClientSide) {
            this.reassessWeaponGoal();
        }
    }

    public int getAttackInterval(){
        return 20;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.SKELETON_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

}
