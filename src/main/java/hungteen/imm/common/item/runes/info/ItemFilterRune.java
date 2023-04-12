package hungteen.imm.common.item.runes.info;

import com.mojang.serialization.Codec;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.common.rune.ICraftableRune;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-05 09:35
 **/
public class ItemFilterRune extends FilterRuneItem<Item> {

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return super.use(level, player, hand);
    }

    @Override
    public Codec<Item> getCodec() {
        return ItemHelper.get().getCodec();
    }

    @Override
    public ICraftableRune getRune() {
        return new ICraftableRune() {
            @Override
            public int requireAmethyst() {
                return 3;
            }

            @Override
            public int requireRedStone() {
                return 3;
            }
        };
    }
}
