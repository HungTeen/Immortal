package hungteen.immortal.common.item.eixirs;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.immortal.common.impl.RealmTypes;
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
        super(Rarity.RARE, ColorHelper.BLACK);
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {
        //TODO 筑基
    }

    @Override
    protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
        return same(RealmTypes.MEDITATION_STAGE10).apply(getRealm(livingEntity));
    }
}
