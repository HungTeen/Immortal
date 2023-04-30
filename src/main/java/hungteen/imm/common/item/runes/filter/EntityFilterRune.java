package hungteen.imm.common.item.runes.filter;

import com.mojang.serialization.Codec;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.common.rune.ICraftableRune;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-20 12:44
 **/
public class EntityFilterRune extends FilterRuneItem<EntityType<?>> {

    private static final ICraftableRune COST = new FilterCraftableRune(3);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(! level.isClientSide() && player.getItemInHand(hand).getItem() instanceof EntityFilterRune rune){
            final HitResult hitResult = PlayerUtil.getHitResult(player, player.getReachDistance());
            if(hitResult instanceof EntityHitResult result){
                rune.bind(player, player.getItemInHand(hand), result.getEntity().getType());
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public ICraftableRune getRune() {
        return COST;
    }

    @Override
    public Codec<EntityType<?>> getCodec() {
        return EntityHelper.get().getCodec();
    }

    @Override
    protected boolean isNumberData() {
        return false;
    }

}
