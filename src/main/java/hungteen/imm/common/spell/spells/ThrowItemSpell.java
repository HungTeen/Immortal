package hungteen.imm.common.spell.spells;

import hungteen.htlib.util.helper.JavaHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.entity.misc.ThrowingItemEntity;
import hungteen.imm.util.EntityUtil;
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
        if (EntityUtil.hasItemInHand(owner)) {
            final ItemStack throwItem = EntityUtil.getItemInHand(owner, JavaHelper::alwaysTrue).copy();
            final ThrowingItemEntity projectile = new ThrowingItemEntity(owner, owner.level());
            projectile.setItem(throwItem);
            projectile.shootFromRotation(owner, owner.getXRot(), owner.getYRot(), 0.0F, 3F, 1.0F);
            owner.level().addFreshEntity(projectile);
            return true;
        } else {
            this.sendTip(owner, "no_item_in_hands");
        }
        return false;
    }

}
