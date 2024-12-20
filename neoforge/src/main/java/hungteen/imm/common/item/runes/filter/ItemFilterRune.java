package hungteen.imm.common.item.runes.filter;

import com.mojang.serialization.Codec;
import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.common.cultivation.rune.ICraftableRune;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-05 09:35
 **/
public class ItemFilterRune extends FilterRuneItem<Item> {

    private static final ICraftableRune COST = new FilterCraftableRune(3);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(! level.isClientSide() && player.getItemInHand(hand).getItem() instanceof ItemFilterRune rune){
            final HitResult hitResult = EntityUtil.getHitResult(player, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE));
            if(hitResult instanceof EntityHitResult result && result.getEntity() instanceof ItemEntity itemEntity && ! itemEntity.getItem().isEmpty()){
                rune.bind(player, player.getItemInHand(hand), itemEntity.getItem().getItem());
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public ICraftableRune getRune() {
        return COST;
    }

    @Override
    public Codec<Item> getCodec() {
        return ItemHelper.get().getCodec();
    }

    @Override
    protected boolean isNumberData() {
        return false;
    }

}
