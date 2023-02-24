package hungteen.immortal.common.item.eixirs;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.common.impl.PlayerRangeNumbers;
import hungteen.immortal.common.impl.RealmTypes;
import hungteen.immortal.utils.PlayerUtil;
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
 * @create: 2022-11-03 17:04
 **/
public abstract class CultivationElixir extends ElixirItem{

    private final int cultivation;

    public CultivationElixir(int cultivation, Rarity rarity, int color) {
        super(rarity, color);
        this.cultivation = cultivation;
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {
        if(! level.isClientSide){
            if(livingEntity instanceof Player){
                PlayerUtil.addIntegerData((Player) livingEntity, PlayerRangeNumbers.CULTIVATION, getCultivation(accuracy));
            }
        }
    }

    public int getCultivation(Accuracies accuracy){
        float multiply = 0;
        switch (accuracy) {
            case COMMON -> multiply = 1;
            case NICE -> multiply = 1.25F;
            case EXCELLENT -> multiply = 1.5F;
            case PERFECT -> multiply = 1.75F;
            case MASTER -> multiply = 2F;
        }
        return (int)(this.cultivation * multiply);
    }

    @Override
    protected List<Object> getUsagesComponentArgs(Accuracies accuracy) {
        return List.of(getCultivation(accuracy));
    }

    public static class FiveFlowersElixir extends CultivationElixir{

        public FiveFlowersElixir() {
            super(1, Rarity.COMMON, ColorHelper.LITTLE_YELLOW1);
        }

        @Override
        protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
            return immortal().apply(ImmortalAPI.get().getEntityRealm(livingEntity));
        }
    }

    public static class GatherBreathElixir extends CultivationElixir{

        public GatherBreathElixir() {
            super(4, Rarity.COMMON, ColorHelper.YELLOW_GREEN);
        }

        @Override
        protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
            return lessEqualThan(RealmTypes.SPIRITUAL_LEVEL_3).apply(ImmortalAPI.get().getEntityRealm(livingEntity));
        }
    }

}
