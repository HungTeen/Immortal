package hungteen.imm.common.spell.spells;

import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.entity.misc.ThrowingItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/17 16:48
 */
public class ThrowItemSpell extends SpellType {

    public ThrowItemSpell() {
        super("throw_item", properties().maxLevel(1).mana(25).cd(80));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if (! owner.getMainHandItem().isEmpty() || ! owner.getOffhandItem().isEmpty()) {
            final ItemStack throwItem = (owner.getMainHandItem().isEmpty() ? owner.getOffhandItem() : owner.getMainHandItem()).copy();
            ThrowingItemEntity projectile = new ThrowingItemEntity(owner, owner.level());
            projectile.setItem(throwItem);
            projectile.shootFromRotation(owner, owner.getXRot(), owner.getYRot(), 0.0F, 3F, 1.0F);
            owner.level().addFreshEntity(projectile);
        }
        return false;
    }
}
