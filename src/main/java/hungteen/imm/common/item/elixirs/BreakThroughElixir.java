package hungteen.imm.common.item.elixirs;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/27 16:18
 */
public class BreakThroughElixir extends ElixirItem{

    public BreakThroughElixir(Rarity rarity, int color) {
        super(rarity, color);
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {

    }

    @Override
    protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
        return Optional.empty();
    }
}
