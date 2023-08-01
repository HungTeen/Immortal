package hungteen.imm.common.item.elixirs;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/27 16:18
 */
public abstract class BreakThroughElixir extends ElixirItem{

    public BreakThroughElixir(Rarity rarity, int color) {
        super(rarity, color);
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {
        if(EntityHelper.isServer(livingEntity) && livingEntity instanceof Player player){
            PlayerUtil.addFloatData(player, PlayerRangeFloats.BREAK_THROUGH_PROGRESS, getBonusProgress(player, player.getRandom(), stack, accuracy));
        }
    }

    public abstract float getBonusProgress(Player player, RandomSource random, ItemStack stack, Accuracies accuracy);

}
