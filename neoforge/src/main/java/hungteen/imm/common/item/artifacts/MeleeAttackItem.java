package hungteen.imm.common.item.artifacts;

import hungteen.imm.api.interfaces.IArtifactTier;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-19 22:37
 **/
public abstract class MeleeAttackItem extends ArtifactItem {

    protected final IMeleeAttackType meleeAttackType;
    protected final float attackDamage;
    /**
     * 攻击速度和秒是倒数关系。
     */
    protected final float attackSpeed;
    protected final float attackRange;

    public MeleeAttackItem(IMeleeAttackType meleeAttackType, IArtifactTier tier) {
        super(new Properties().stacksTo(1).durability(tier.getUses())
                .attributes(create(meleeAttackType, tier)), tier.getArtifactRealm());
        this.meleeAttackType = meleeAttackType;
        this.attackDamage = tier.getAttackDamage() + this.meleeAttackType.getBaseAttackDamage();
        this.attackSpeed = tier.getAttackSpeed() + this.meleeAttackType.getBaseAttackSpeed();
        this.attackRange = tier.getAttackRange() + this.meleeAttackType.getBaseAttackRange();
    }

    public static ItemAttributeModifiers create(IMeleeAttackType meleeAttackType, IArtifactTier tier) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Util.prefix("Artifact modifier"), tier.getAttackDamage() + meleeAttackType.getBaseAttackDamage(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Util.prefix("Artifact modifier"), tier.getAttackSpeed() + meleeAttackType.getBaseAttackSpeed(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(Util.prefix("Artifact modifier"), tier.getAttackRange() + meleeAttackType.getBaseAttackRange(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public @NotNull AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
        final double range = Objects.requireNonNull(player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE)).getValue();
        return target.getBoundingBox().inflate(range / 3, 0.25D, range / 3);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity enemy, LivingEntity livingEntity) {
        if(livingEntity.level() instanceof ServerLevel serverLevel){
            stack.hurtAndBreak(getHurtEnemyCost(livingEntity, stack, enemy), serverLevel, livingEntity, (p) -> {
                livingEntity.onEquippedItemBroken(stack.getItem(), EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos blockPos, LivingEntity livingEntity) {
        if(livingEntity.level() instanceof ServerLevel serverLevel && state.getDestroySpeed(level, blockPos) != 0.0F){
            stack.hurtAndBreak(getMineBlockCost(livingEntity, stack, state), serverLevel, livingEntity, (p) -> {
                livingEntity.onEquippedItemBroken(stack.getItem(), EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    public int getHurtEnemyCost(LivingEntity livingEntity, ItemStack stack, LivingEntity enemy) {
        return 1;
    }

    public int getMineBlockCost(LivingEntity livingEntity, ItemStack stack, BlockState state){
        return 2;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility ability) {
        return this.meleeAttackType.canSweep() && ability == ItemAbilities.SWORD_SWEEP;
    }

    public interface IMeleeAttackType{

        /**
         * Basic Melee Attack Damage Getter.
         * @return attack damage value.
         */
        float getBaseAttackDamage();

        /**
         * Basic Melee Attack Speed Getter.
         * @return attack speed value.
         */
        float getBaseAttackSpeed();

        /**
         * Basic Melee Attack Range Getter.
         * @return attack range value.
         */
        float getBaseAttackRange();

        /**
         * Sweep action checker.
         * @return true means can do sweep action.
         */
        boolean canSweep();

    }

    public enum MeleeAttackTypes implements IMeleeAttackType{
        // 剑 DPS : 12.8, REACH : 3.2
        SWORD(8F, -2.4F, 0.2F, true),
        // 短剑 DPS : 16, REACH : 1.5
        SHORT_SWORD(4F, 0F, -1.5F, false),
        // 斧 DPS : 12, REACH : 3
        AXE(12F, -3F, 0F, false),
        // 镐 DPS : 7.5, REACH : 3
        PICKAXE(6F, -2.75F, 0F, false),
        // 铲 DPS : 7.5, REACH : 3
        SHOVEL(5F, -2.5F, 0F, false),
        // 锄 DPS : 4, REACH : 3
        HOE(1F, 0F, 0F, false),
        // 棍棒 DPS : 7.2, REACH : 4.5
        STICK(2F, -0.4F, 1.5F, true),
        // 锤 DPS : 8, REACH : 3
        HAMMER(16F, -3.5F, 0F, false),
        // 刀 DPS : 16, REACH : 3
        MACHETE(8F, -2F, 0F, true),
        // 枪 DPS : 10.8, REACH : 4.5
        SPEAR(9F, -2.8F, 1.5F, true),
        // 戟 DPS : 10, REACH : 4.5
        HALBERT(10F, -3F, 1.5F, true),
        ;

        private final float attackDamage;
        private final float attackSpeed;
        private final float attackRange;
        protected final boolean canSweep;

        MeleeAttackTypes(float attackDamage, float attackSpeed, float attackRange, boolean canSweep) {
            this.attackDamage = attackDamage;
            this.attackSpeed = attackSpeed;
            this.attackRange = attackRange;
            this.canSweep = canSweep;
        }

        @Override
        public float getBaseAttackDamage() {
            return attackDamage;
        }

        @Override
        public float getBaseAttackSpeed() {
            return attackSpeed;
        }

        @Override
        public float getBaseAttackRange() {
            return attackRange;
        }

        @Override
        public boolean canSweep() {
            return canSweep;
        }
    }
}
