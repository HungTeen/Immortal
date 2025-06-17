package hungteen.imm.common.cultivation.spell.common;

import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/6 17:02
 */
public class PickupItemSpell extends SpellTypeImpl {

    public PickupItemSpell() {
        super("pickup_item", property().maxLevel(2).mana(5).cd(20));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        if(context.targetOpt().isPresent() && context.owner() instanceof Player player){
            Optional<InteractionHand> handOpt = EntityUtil.getEmptyHand(context.owner());
            if(handOpt.isEmpty()){
                sendTip(context.owner(), NO_EMPTY_HAND);
                return false;
            }
            if (context.target() instanceof ItemEntity item) {
                item.setPos(context.owner().position());
                item.setPickUpDelay(0);
                context.owner().swing(handOpt.get());
                return true;
            }
            if (context.spellLevel() > 1 && context.targetOpt().get() instanceof ItemSupplier sup) {
                ItemStack stack = sup.getItem().copy();
                PlayerUtil.addItem(player, stack);
                context.owner().swing(handOpt.get());
                return true;
            }
        }
        return false;
    }

}
