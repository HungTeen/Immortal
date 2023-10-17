package hungteen.imm.common.effect;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.util.Util;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 14:39
 **/
public class IMMEffects {

    private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Util.id());

    public static final RegistryObject<MobEffect> BREAK_THROUGH = EFFECTS.register("break_through", () -> new NoCureMobEffect(MobEffectCategory.BENEFICIAL, ColorHelper.AQUA.rgb()));

    public static final RegistryObject<MobEffect> OPPRESSION = EFFECTS.register("oppression", () -> new NoCureMobEffect(MobEffectCategory.HARMFUL, ColorHelper.DARK_RED.rgb())
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "96c795e1-dbde-4512-a0a7-13489da62f7e", -0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, "6960b14b-d46f-47de-a758-d8233249fd23", -0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED, "13aaad41-0289-4375-8b20-6d129a6eee80", -0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.FLYING_SPEED, "2f9401f7-e4d2-4b27-bd23-b97f938f17d0", -0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL)
    );

    public static final RegistryObject<MobEffect> SOLIDIFICATION = EFFECTS.register("solidification", () -> new NoCureMobEffect(MobEffectCategory.HARMFUL, ColorHelper.DYE_BROWN.rgb())
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "98509b83-d422-4b4e-9633-bb2a9e57af6a", -0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL)
    );

//    public static final RegistryObject<MobEffect> CORPSE_POISON_EFFECT = EFFECTS.register("corpse_poison", () -> {
//        return new NoCureMobEffect(MobEffectCategory.HARMFUL, ColorHelper.DARK_GREEN.rgb());
//    });

    public static void register(IEventBus event) {
        EFFECTS.register(event);
    }

}
