package hungteen.immortal.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 14:57
 **/
public abstract class ImmortalMobEffect extends MobEffect {

    public ImmortalMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int lvl) {

    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity effectEntity, @Nullable Entity owner, LivingEntity entity, int level, double distance) {

    }

    @Override
    public boolean isDurationEffectTick(int time, int level) {
        return false;
    }
}
