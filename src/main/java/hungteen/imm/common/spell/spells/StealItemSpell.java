package hungteen.imm.common.spell.spells;

import hungteen.imm.api.EntityBlockResult;
import hungteen.imm.common.spell.SpellTypes;
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
 * @data 2023/3/6 17:02
 */
public class StealItemSpell extends SpellTypes.SpellType {

    public StealItemSpell(SpellTypes.SpellProperties properties) {
        super("item_stealing", properties);
    }

    @Override
    public boolean onActivate(LivingEntity owner, EntityBlockResult result, int level) {
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

    @Override
    public boolean isPassiveSpell() {
        return false;
    }
}