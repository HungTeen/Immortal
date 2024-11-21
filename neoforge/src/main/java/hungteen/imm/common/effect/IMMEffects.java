package hungteen.imm.common.effect;

import hungteen.htlib.api.registry.PTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 14:39
 **/
public class IMMEffects {

    private static final HTVanillaRegistry<MobEffect> EFFECTS = HTRegistryManager.vanilla(Registries.MOB_EFFECT, Util.id());

    public static final PTHolder<MobEffect> BREAK_THROUGH = EFFECTS.registerForHolder("break_through", () -> new NoCureMobEffect(MobEffectCategory.BENEFICIAL, ColorHelper.AQUA.rgb()));

    public static final PTHolder<MobEffect> OPPRESSION = EFFECTS.registerForHolder("oppression", () -> new NoCureMobEffect(MobEffectCategory.HARMFUL, ColorHelper.DARK_RED.rgb())
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, Util.prefix("96c795e1-dbde-4512-a0a7-13489da62f7e"), -0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, Util.prefix("6960b14b-d46f-47de-a758-d8233249fd23"), -0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED, Util.prefix("13aaad41-0289-4375-8b20-6d129a6eee80"), -0.2F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.FLYING_SPEED, Util.prefix("2f9401f7-e4d2-4b27-bd23-b97f938f17d0"), -0.2F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    public static final PTHolder<MobEffect> SOLIDIFICATION = EFFECTS.registerForHolder("solidification", () -> new NoCureMobEffect(MobEffectCategory.HARMFUL, ColorHelper.DYE_BROWN.rgb())
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, Util.prefix("98509b83-d422-4b4e-9633-bb2a9e57af6a"), -0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

//    public static final RegistryObject<MobEffect> CORPSE_POISON_EFFECT = EFFECTS.initialize("corpse_poison", () -> {
//        return new NoCureMobEffect(MobEffectCategory.HARMFUL, ColorHelper.DARK_GREEN.rgb());
//    });

    public static void initialize(IEventBus event) {
        NeoHelper.initRegistry(EFFECTS, event);
    }

}
