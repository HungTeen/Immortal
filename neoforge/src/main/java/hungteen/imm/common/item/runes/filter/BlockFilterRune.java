package hungteen.imm.common.item.runes.filter;

import com.mojang.serialization.Codec;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.common.rune.ICraftableRune;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-20 12:41
 **/
public class BlockFilterRune extends FilterRuneItem<Block> {

    private static final ICraftableRune COST = new FilterCraftableRune(3);

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getItemInHand().getItem() instanceof BlockFilterRune rune && context.getPlayer() != null){
            if(! context.getLevel().isClientSide()){
                rune.bind(context.getPlayer(), context.getItemInHand(), context.getLevel().getBlockState(context.getClickedPos()).getBlock());
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    public ICraftableRune getRune() {
        return COST;
    }

    @Override
    public Codec<Block> getCodec() {
        return BlockHelper.get().getCodec();
    }

    @Override
    protected boolean isNumberData() {
        return false;
    }

}
