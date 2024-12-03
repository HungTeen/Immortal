package hungteen.imm.common.effect;

import hungteen.htlib.util.helper.impl.EffectHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Set;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/3 17:22
 **/
public class CorpsePoisonEffect extends HTMobEffect {

    public CorpsePoisonEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        // 丹药才能治疗。
    }

    /**
     * 附加尸毒效果，如果已经有尸毒效果则叠加等级。
     */
    public static void attachPoison(LivingEntity target, int level, int duration) {
        MobEffectInstance effect = target.getEffect(IMMEffects.CORPSE_POISON.holder());
        target.addEffect(EffectHelper.viewEffect(IMMEffects.CORPSE_POISON.holder(), duration, level + (effect == null ? 0 : effect.getAmplifier())));
    }

}
