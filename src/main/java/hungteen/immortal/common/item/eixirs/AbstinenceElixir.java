package hungteen.immortal.common.item.eixirs;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.EffectHelper;
import hungteen.immortal.impl.RealmTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 11:12
 **/
public class AbstinenceElixir extends ElixirItem{

    public AbstinenceElixir() {
        super(Rarity.COMMON, ColorHelper.WHITE);
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {
        if(! level.isClientSide){
            livingEntity.addEffect(EffectHelper.effect(MobEffects.SATURATION, getDuration(accuracy), 2));
        }
    }

    public int getDuration(Accuracies accuracy){
        switch (accuracy) {
            case COMMON: return 24000;
            case NICE: return 30000;
            case EXCELLENT: return 36000;
            case PERFECT: return 48000;
            case MASTER: return 72000;
            default: return 0;
        }
    }

    @Override
    protected List<Object> getUsagesComponentArgs(Accuracies accuracy) {
        return List.of(getDuration(accuracy) / 20);
    }

    @Override
    protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
        return lessThan(RealmTypes.FOUNDATION_BEGIN).apply(getRealm(livingEntity));
    }
}
