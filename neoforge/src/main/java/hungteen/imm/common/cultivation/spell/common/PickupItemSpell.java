package hungteen.imm.common.cultivation.spell.common;

import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
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
        super("pickup_item", properties().maxLevel(2).mana(5).cd(20));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if (result.hasEntity() && owner instanceof Player player) {
            Optional<InteractionHand> handOpt = EntityUtil.getEmptyHand(owner);
            if(handOpt.isEmpty()){
                this.sendTip(owner, "no_empty_hand");
                return false;
            }
            if (result.getEntity() instanceof ItemEntity item) {
                item.setPos(owner.position());
                item.setPickUpDelay(0);
                owner.swing(handOpt.get());
                return true;
            }
            if (level > 1) {
                if (result.getEntity() instanceof ItemSupplier sup) {
                    ItemStack stack = sup.getItem().copy();
                    PlayerUtil.addItem(player, stack);
                    owner.swing(handOpt.get());
                    return true;
                }
            }
        }
        return false;
    }

}
