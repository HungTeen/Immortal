package hungteen.imm.common.item.elixirs;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 10:26
 **/
public abstract class SpiritualElixir extends ElixirItem{

    private final int spiritualValue;

    public SpiritualElixir(int spiritualValue, Rarity rarity, int color) {
        super(rarity, color);
        this.spiritualValue = spiritualValue;
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {
        if(! level.isClientSide){
            if(livingEntity instanceof Player){
                PlayerUtil.addFloatData((Player) livingEntity, PlayerRangeFloats.SPIRITUAL_MANA, getSpiritualValue(accuracy));
            }
        }
    }

    protected int getSpiritualValue(Accuracies accuracy){
        float multiply = 0;
        switch (accuracy) {
            case COMMON -> multiply = 1;
            case NICE -> multiply = 1.25F;
            case EXCELLENT -> multiply = 1.6F;
            case PERFECT -> multiply = 2F;
            case MASTER -> multiply = 2.5F;
        }
        return (int)(this.spiritualValue * multiply);
    }

    @Override
    protected List<Object> getUsagesComponentArgs(Accuracies accuracy) {
        return List.of(getSpiritualValue(accuracy));
    }

    public static class SpiritRecoveryElixir extends SpiritualElixir{

        public SpiritRecoveryElixir() {
            super(50, Rarity.COMMON, ColorHelper.IRIS_BLUE.rgb());
        }

        @Override
        protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
            return immortal().apply(getRealm(livingEntity));
        }
    }

}
