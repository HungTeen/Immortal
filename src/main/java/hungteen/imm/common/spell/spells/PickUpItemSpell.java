package hungteen.imm.common.spell.spells;

import hungteen.imm.api.EntityBlockResult;
import hungteen.imm.common.spell.SpellTypes;
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
public class PickUpItemSpell extends SpellTypes.SpellType {

    public PickUpItemSpell(SpellTypes.SpellProperties properties) {
        super("item_picking", properties);
    }

    @Override
    public boolean onActivate(LivingEntity owner, EntityBlockResult result, int level) {
        if (result.hasEntity() && owner instanceof Player player) {
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

    @Override
    public boolean isPassiveSpell() {
        return false;
    }
}
