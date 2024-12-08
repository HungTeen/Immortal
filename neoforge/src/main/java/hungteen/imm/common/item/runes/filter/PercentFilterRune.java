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
 * @create 2023-04-26 22:00
 **/
public class PercentFilterRune extends FilterRuneItem<Float>{

    private static final ICraftableRune COST = new FilterCraftableRune(2);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if(! level.isClientSide() && stack.getItem() instanceof PercentFilterRune item && ! item.alreadyBind(stack)){
            final float cur = getData(stack).orElse(1F);
            item.bind(player, player.getItemInHand(hand), cur == 1F ? 0F : cur + 0.1F);
        }
        return super.use(level, player, hand);
    }

    @Override
    public Codec<Float> getCodec() {
        return Codec.FLOAT;
    }

    @Override
    protected boolean isNumberData() {
        return true;
    }

    @Override
    public ICraftableRune getRune() {
        return COST;
    }

}
