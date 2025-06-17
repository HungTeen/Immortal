package hungteen.imm.common.cultivation.spell.metal;

import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2023-08-28 18:25
 **/
public class MetalMendingSpell extends SpellTypeImpl {

    private static final float MEND_PERCENT = 0.12F;

    public MetalMendingSpell() {
        super("metal_mending", property().mana(80).cd(200).maxLevel(1));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        return super.checkActivate(context);
    }

//    @Override
//    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
//        ItemStack mainItem = owner.getMainHandItem();
//        ItemStack offItem = owner.getOffhandItem();
//        if (checkRepairable(mainItem, offItem)) {
//            repairItem(owner, mainItem, offItem);
//            return true;
//        } else if (checkRepairable(offItem, mainItem)) {
//            repairItem(owner, offItem, mainItem);
//            return true;
//        }
//        this.sendTip(owner, "can_not_repair");
//        return false;
//    }

    protected void repairItem(LivingEntity owner, ItemStack stack, ItemStack ingredient) {
        final int repairCount = Mth.ceil(stack.getMaxDamage() * MEND_PERCENT);
        final int toRepair = Math.min(stack.getDamageValue(), repairCount);
        if (toRepair > 0) {
            stack.setDamageValue(stack.getDamageValue() - toRepair);
            ingredient.shrink(1);
            owner.playSound(SoundEvents.ANVIL_USE);
        }
    }

    private static boolean checkRepairable(ItemStack mainItem, ItemStack offItem) {
        return mainItem.isRepairable() && mainItem.getItem().isValidRepairItem(mainItem, offItem);
    }
}
