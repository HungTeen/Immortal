package hungteen.immortal.common.item.eixirs;

import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.impl.PlayerDatas;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 10:26
 **/
public abstract class SpiritualElixir extends ElixirItem{

    private final int spiritualValue;

    public SpiritualElixir(int spiritualValue, Rarity rarity) {
        super(rarity);
        this.spiritualValue = spiritualValue;
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {
        if(! level.isClientSide){
            if(livingEntity instanceof Player){
                PlayerUtil.addIntegerData((Player) livingEntity, PlayerDatas.SPIRITUAL_MANA, getSpiritualValue(accuracy));
            }
        }
    }

    protected int getSpiritualValue(Accuracies accuracy){
        return accuracy == Accuracies.NICE ? (int) (this.spiritualValue * 1.2F) :
                        accuracy == Accuracies.PERFECT ? (int) (this.spiritualValue * 1.5F) :
                                accuracy == Accuracies.MASTER ? (int) (this.spiritualValue * 2F) :
                                        this.spiritualValue;
    }

    public static class SpiritRecoveryElixir extends SpiritualElixir{

        public SpiritRecoveryElixir() {
            super(50, Rarity.COMMON);
        }

        @Override
        protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
            return immortal().apply(getRealm(livingEntity));
        }
    }

}
