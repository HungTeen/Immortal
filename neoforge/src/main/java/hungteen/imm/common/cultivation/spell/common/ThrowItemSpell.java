package hungteen.imm.common.cultivation.spell.common;

import hungteen.htlib.util.helper.JavaHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.common.entity.misc.ThrowingItemEntity;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.LevelUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/17 16:48
 */
public class ThrowItemSpell extends SpellTypeImpl {

    public ThrowItemSpell() {
        super("throw_item", property(SpellUsageCategory.ATTACK_TARGET, InscriptionTypes.ANY).maxLevel(1).mana(25).cd(80));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        ItemStack throwItem = ItemStack.EMPTY;
        if(context.itemTrigger()){
            throwItem = context.usingItem();
        } else if(EntityUtil.hasItemInHand(context.owner())){
            throwItem = EntityUtil.getItemInHand(context.owner(), JavaHelper::alwaysTrue);
        }
        if(! throwItem.isEmpty()) {
            // TODO 御物术 II
            final ThrowingItemEntity projectile = new ThrowingItemEntity(context.owner(), context.level());
            projectile.setItem(throwItem.copy());
            if(context.owner() instanceof Player || context.targetStateOpt().isEmpty()){
                projectile.shootFromRotation(context.owner(), context.owner().getXRot(), context.owner().getYRot(), 0.0F, 3F, 1.0F);
            } else {
                EntityUtil.shootProjectile(projectile, context.target().position().subtract(context.owner().getEyePosition()), 1.2F, 2F);
            }
            if(context.level().addFreshEntity(projectile)){
                if(! EntityUtil.notConsume(context.owner())){
                    throwItem.shrink(1);
                }
                LevelUtil.playSound(context.level(), SoundEvents.TRIDENT_THROW.value(), SoundSource.NEUTRAL, projectile.position(), 1F, 1F);
                return true;
            } else {
                IMMAPI.logger().error("Failed to throw item entity");
            }
        }
        sendTip(context.owner(), NO_ITEM_IN_HANDS);
        return false;
    }

}
