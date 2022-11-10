package hungteen.immortal.common.effect;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.immortal.utils.Util;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 14:39
 **/
public class ImmortalEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Util.id());

    public static final RegistryObject<MobEffect> CORPSE_POISON_EFFECT = EFFECTS.register("corpse_poison", () -> {
        return new NoCureMobEffect(MobEffectCategory.HARMFUL, ColorHelper.DARK_GREEN);
    });

}
