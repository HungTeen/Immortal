package hungteen.immortal.common.item.eixirs;

import hungteen.htlib.util.EffectUtil;
import hungteen.immortal.impl.Realms;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 11:12
 **/
public class AbstinenceElixir extends ElixirItem{

    public AbstinenceElixir() {
        super(Rarity.COMMON);
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {
        if(! level.isClientSide){
            int time = accuracy == Accuracies.NICE ? 36000 :
                            accuracy == Accuracies.PERFECT ? 48000 :
                                    accuracy == Accuracies.MASTER ? 72000 :
                                            24000;
            livingEntity.addEffect(EffectUtil.effect(MobEffects.SATURATION, time, 2));
        }
    }

    @Override
    protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
        return lessThan(Realms.FOUNDATION_BEGIN).apply(getRealm(livingEntity));
    }
}
