package hungteen.imm.common.item.talismans;

import hungteen.imm.util.EntityUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-04 16:16
 **/
public abstract class DurationTalismanItem extends TalismanItem {

    public DurationTalismanItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.canUse(stack, player)) {
            player.startUsingItem(hand);
            setActivated(stack, true);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(! level.isClientSide && ! EntityUtil.notConsume(entity)){
            stack.shrink(1);
        }
        return stack;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int tick) {
        setActivated(stack, false);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity living) {
        return 30;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CUSTOM;
    }
}
