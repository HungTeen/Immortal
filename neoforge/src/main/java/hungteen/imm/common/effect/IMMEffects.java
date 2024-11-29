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
import net.neoforged.neoforge.common.NeoForgeMod;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 14:39
 **/
public class IMMEffects {

    private static final HTVanillaRegistry<MobEffect> EFFECTS = HTRegistryManager.vanilla(Registries.MOB_EFFECT, Util.id());

    public static final PTHolder<MobEffect> BREAK_THROUGH = EFFECTS.registerForHolder("break_through", () -> new NoCureMobEffect(MobEffectCategory.BENEFICIAL, ColorHelper.AQUA.rgb()));

    public static final PTHolder<MobEffect> OPPRESSION = EFFECTS.registerForHolder("oppression", () -> new NoCureMobEffect(MobEffectCategory.HARMFUL, ColorHelper.DARK_RED.rgb())
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, Util.prefix("effect.oppression"), -0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, Util.prefix("effect.oppression"), -0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED, Util.prefix("effect.oppression"), -0.2F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.FLYING_SPEED, Util.prefix("effect.oppression"), -0.2F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    public static final PTHolder<MobEffect> SOLIDIFICATION = EFFECTS.registerForHolder("solidification", () -> new NoCureMobEffect(MobEffectCategory.HARMFUL, ColorHelper.DYE_BROWN.rgb())
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, Util.prefix("effect.solidification"), -0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.FLYING_SPEED, Util.prefix("effect.solidification"), -0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.SNEAKING_SPEED, Util.prefix("effect.solidification"), -0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(NeoForgeMod.SWIM_SPEED, Util.prefix("effect.solidification"), -0.2F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    public static final PTHolder<MobEffect> CUTTING = EFFECTS.registerForHolder("cutting", () -> new NoCureMobEffect(MobEffectCategory.HARMFUL, ColorHelper.DARK_RED.rgb()));

//    public static final RegistryObject<MobEffect> CORPSE_POISON_EFFECT = EFFECTS.initialize("corpse_poison", () -> {
//        return new NoCureMobEffect(MobEffectCategory.HARMFUL, ColorHelper.DARK_GREEN.rgb());
//    });

    public static void initialize(IEventBus event) {
        NeoHelper.initRegistry(EFFECTS, event);
    }

}
