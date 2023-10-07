package hungteen.imm.common.spell.spells;

import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.util.EntityUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-28 18:25
 **/
public class MetalMendingSpell extends SpellType {

    private static final int MEND_VALUE = 10;

    public MetalMendingSpell() {
        super("metal_mending", properties().mana(80).cd(200).maxLevel(1));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        final ItemStack stack = EntityUtil.getItemInHand(owner, itemStack -> {
            return ItemHelper.get().getTagList(Tags.Items.INGOTS).stream().anyMatch(ingot -> {
                return itemStack.isDamageableItem() && itemStack.getItem().isValidRepairItem(itemStack, new ItemStack(ingot));
            });
        });
        if(! stack.isEmpty()){
            final int toRepair = Math.min(stack.getDamageValue(), MEND_VALUE);
            if(toRepair > 0){
                stack.setDamageValue(stack.getDamageValue() - toRepair);
                owner.playSound(SoundEvents.ANVIL_USE);
                return true;
            }
        }
        this.sendTip(owner, "can_not_repair");
        return false;
    }
}
