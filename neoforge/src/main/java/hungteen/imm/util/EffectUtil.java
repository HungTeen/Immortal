package hungteen.imm.util;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/29 22:12
 **/
public class EffectUtil {

    public static int getEffectLevel(LivingEntity living, Holder<MobEffect> effect){
        return living.hasEffect(effect) ? Objects.requireNonNull(living.getEffect(effect)).getAmplifier() : 0;
    }
}
