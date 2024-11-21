package hungteen.imm.common.spell.spells.common;

import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.spell.spells.SpellType;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/6 17:02
 */
public class StealItemSpell extends SpellType {

    public StealItemSpell() {
        super("steal_item", properties().maxLevel(1).mana(50).cd(1800));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if (result.getEntity() instanceof LivingEntity living && owner instanceof Player player) {
                List<ItemStack> items = Arrays.stream(EquipmentSlot.values())
                        .map(living::getItemBySlot)
                        .filter(s -> ! s.isEmpty())
                        .toList();
                if(items.size() == 0) return false;
                ItemStack oldStack = items.get(owner.getRandom().nextInt(items.size()));
                ItemStack stack = oldStack.copy();
                PlayerUtil.addItem(player, stack);
                oldStack.setCount(0);
                return true;
        }
        return false;
    }

}
