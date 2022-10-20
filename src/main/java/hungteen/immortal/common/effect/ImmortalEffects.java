package hungteen.immortal.common.effect;

import hungteen.htlib.util.ColorUtil;
import hungteen.immortal.utils.Util;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import org.jline.utils.Colors;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 14:39
 **/
public class ImmortalEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Util.id());

    public static final RegistryObject<MobEffect> CORPSE_POISON_EFFECT = EFFECTS.register("corpse_poison", () -> {
        return new NoCureMobEffect(MobEffectCategory.HARMFUL, ColorUtil.DARK_GREEN);
    });

}
