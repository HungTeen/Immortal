package hungteen.imm.common.entity.undead;

import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.common.cultivation.realm.MultiRealm;
import hungteen.imm.common.effect.CorpsePoisonEffect;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.entity.ai.goal.AggressiveAttackGoal;
import hungteen.imm.common.entity.ai.goal.UseSpellGoal;
import hungteen.imm.util.ItemUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;

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

    public QiZombie(EntityType<? extends UndeadEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return IMMMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24F)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 2.0D)
                ;
    }

    @Override
    protected void addTargetGoals() {
        super.addTargetGoals();
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new UseSpellGoal(this));
        this.goalSelector.addGoal(2, new AggressiveAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void serverFinalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType) {
        super.serverFinalizeSpawn(accessor, difficultyInstance, spawnType);
        if (accessor.getRandom().nextFloat() < 0.05F) {
            this.setBaby(true);
        }
    }

    @Override
    public List<Spell> getRandomSpells(RandomSource random, Element element, RealmType realm) {
        return switch (element) {
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
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (flag && this.level() instanceof ServerLevel serverLevel) {
            LivingEntity target = this.getTarget();
            if (target == null && source.getEntity() instanceof LivingEntity) {
                target = (LivingEntity) source.getEntity();
            }

            if (target != null && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                // 业灵根或者高境界可以召唤援军。
                if ((this.hasElement(Element.SPIRIT) && this.getRandom().nextFloat() < 0.2)
                        || (RealmTypes.MID_RANK_UNDEAD.hasRealm(getRealm()) && this.getRandom().nextFloat() < 0.05)) {
                    spawnReinforcements(serverLevel, target, 25);
                }
            }
        }

        return flag;
    }

    /**
     * 召唤僵尸援军。
     */
    protected void spawnReinforcements(ServerLevel serverLevel, LivingEntity target, int tries) {
        int i = Mth.floor(this.getX());
        int j = Mth.floor(this.getY());
        int k = Mth.floor(this.getZ());
        QiZombie zombie = new QiZombie(IMMEntities.QI_ZOMBIE.get(), this.level());

        for (int l = 0; l < tries; l++) {
            int i1 = i + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
            int j1 = j + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
            int k1 = k + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
            BlockPos blockpos = new BlockPos(i1, j1, k1);
            EntityType<?> entitytype = zombie.getType();
            if (SpawnPlacements.isSpawnPositionOk(entitytype, this.level(), blockpos) && SpawnPlacements.checkSpawnRules(entitytype, serverLevel, MobSpawnType.REINFORCEMENT, blockpos, this.level().random)) {
                zombie.setPos(i1, j1, k1);
                if (!serverLevel.hasNearbyAlivePlayer(i1, j1, k1, 7.0)
                        && serverLevel.isUnobstructed(zombie)
                        && serverLevel.noCollision(zombie)
                        && !serverLevel.containsAnyLiquid(zombie.getBoundingBox())) {
                    zombie.setTarget(target);
                    EventHooks.finalizeMobSpawn(zombie, serverLevel, serverLevel.getCurrentDifficultyAt(zombie.blockPosition()), MobSpawnType.REINFORCEMENT, null);
                    serverLevel.addFreshEntityWithPassengers(zombie);
                    ParticleHelper.sendParticles(serverLevel, IMMParticles.SPIRIT_ELEMENT.get(), getX(), getEyeY(), getZ(), 15, 0.2, 0.2, 0.2, 0.05);
                    break;
                }
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
                entity.igniteForSeconds((float) (2 * (int) f));
            }

            // 附加尸毒。
            if (entity instanceof LivingEntity target && this.hasElement(Element.WOOD)) {
                if (this.getRandom().nextFloat() < 0.5F / this.getRoots().size()) {
                    CorpsePoisonEffect.attachPoison(target, 1, getRoots().size() == 1 ? 1200 : 600);
                }
            }
        }

        return flag;
    }

    @Override
    public List<MultiRealm> getMultiRealms() {
        return List.of(RealmTypes.LOW_RANK_UNDEAD, RealmTypes.MID_RANK_UNDEAD);
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
    public void setBaby(boolean baby) {
        super.setBaby(baby);
        if (!this.level().isClientSide) {
            AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attributeinstance != null) {
                if (baby) {
                    attributeinstance.addOrUpdateTransientModifier(SPEED_MODIFIER_BABY);
                } else {
                    attributeinstance.removeModifier(SPEED_MODIFIER_BABY);
                }
            }
        }
    }

}
