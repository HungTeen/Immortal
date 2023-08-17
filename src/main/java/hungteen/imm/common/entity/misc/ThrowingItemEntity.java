package hungteen.imm.common.entity.misc;

import hungteen.imm.common.entity.IMMEntities;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/17 16:52
 */
public class ThrowingItemEntity extends ThrowableItemProjectile {

    public ThrowingItemEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public ThrowingItemEntity(LivingEntity thrower, Level level) {
        super(IMMEntities.THROWING_ITEM.get(), thrower, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        double damage = 1F;
        for (AttributeModifier modifier : this.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE)) {
            if (modifier.getId() == Item.BASE_ATTACK_DAMAGE_UUID) {
                damage += EnchantmentHelper.getDamageBonus(this.getItem(), MobType.UNDEFINED);
                break;
            }
        }
        Entity entity = result.getEntity();
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float) damage);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.getItem().getItem() instanceof BlockItem ? EntityDimensions.scalable(1F, 1F) : EntityDimensions.scalable(0.9F, 0.2F);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

}
