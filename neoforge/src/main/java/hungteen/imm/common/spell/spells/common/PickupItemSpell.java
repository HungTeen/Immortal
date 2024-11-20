package hungteen.imm.common.spell.spells.common;

import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.spell.spells.SpellType;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/6 17:02
 */
public class PickupItemSpell extends SpellType {

    public PickupItemSpell() {
        super("pickup_item", properties().maxLevel(2).mana(5).cd(20));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if (result.hasEntity() && owner instanceof Player player) {
            if(! EntityUtil.hasEmptyHand(owner)){
                this.sendTip(owner, "no_empty_hand");
                return false;
            }
            if (result.getEntity() instanceof ItemEntity item) {
                item.setPos(owner.position());
                item.setPickUpDelay(0);
                return true;
            }
            if (level > 1) {
                if (result.getEntity() instanceof ItemSupplier sup) {
                    ItemStack stack = sup.getItem().copy();
                    PlayerUtil.addItem(player, stack);
                    return true;
                }
            }
        }
        return false;
    }

}
