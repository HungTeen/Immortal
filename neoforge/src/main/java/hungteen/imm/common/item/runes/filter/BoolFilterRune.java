package hungteen.imm.common.item.runes.filter;

import com.mojang.serialization.Codec;
import hungteen.imm.common.cultivation.rune.ICraftableRune;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-26 21:51
 **/
public class BoolFilterRune extends FilterRuneItem<Boolean>{

    private static final ICraftableRune COST = new FilterCraftableRune(1);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if(! level.isClientSide() && stack.getItem() instanceof BoolFilterRune item && ! item.alreadyBind(stack)){
            final boolean cur = getData(stack).orElse(false);
            item.bind(player, player.getItemInHand(hand), !cur);
        }
        return super.use(level, player, hand);
    }

    @Override
    public Codec<Boolean> getCodec() {
        return Codec.BOOL;
    }

    @Override
    protected boolean isNumberData() {
        return false;
    }

    @Override
    public ICraftableRune getRune() {
        return COST;
    }

}
