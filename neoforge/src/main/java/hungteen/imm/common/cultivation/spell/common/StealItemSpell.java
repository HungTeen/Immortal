package hungteen.imm.common.cultivation.spell.common;

import hungteen.imm.common.cultivation.spell.SpellTypeImpl;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/6 17:02
 */
public class StealItemSpell extends SpellTypeImpl {

    public StealItemSpell() {
        super("steal_item", property().maxLevel(1).mana(50).cd(1800));
    }

//    @Override
//    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
//        if (result.getEntity() instanceof LivingEntity living && owner instanceof Player player) {
//                List<ItemStack> items = Arrays.stream(EquipmentSlot.values())
//                        .map(living::getItemBySlot)
//                        .filter(s -> ! s.isEmpty())
//                        .toList();
//                if(items.isEmpty()) {
//                    return false;
//                }
//                ItemStack oldStack = items.get(owner.getRandom().nextInt(items.size()));
//                ItemStack stack = oldStack.copy();
//                PlayerUtil.addItem(player, stack);
//                oldStack.setCount(0);
//                return true;
//        }
//        return false;
//    }

}
