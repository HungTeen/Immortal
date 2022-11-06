package hungteen.immortal.common.item.eixirs;

import hungteen.htlib.util.ColorUtil;
import hungteen.immortal.impl.Realms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 12:23
 **/
public class FoundationElixir extends ElixirItem{

    public FoundationElixir() {
        super(Rarity.RARE, ColorUtil.BLACK);
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {
        //TODO 筑基
    }

    @Override
    protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
        return same(Realms.MEDITATION_STAGE10).apply(getRealm(livingEntity));
    }
}
