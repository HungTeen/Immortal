package hungteen.imm.common;

import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-18 20:41
 **/
public interface IMMDamageTypes {

    ResourceKey<DamageType> QI = create("qi");
    ResourceKey<DamageType> SPIRITUAL_FLAME = create("spiritual_flame");
    ResourceKey<DamageType> ELEMENT_REACTION = create("element_reaction");
    ResourceKey<DamageType> WOOD_ELEMENT = create("wood_element");
    ResourceKey<DamageType> WATER_ELEMENT = create("water_element");
    ResourceKey<DamageType> FIRE_ELEMENT = create("fire_element");

    static void register(BootstrapContext<DamageType> context) {
        register(context, QI, 0.05F);
        register(context, SPIRITUAL_FLAME, 0.06F);
        register(context, ELEMENT_REACTION, 0F);
        register(context, WOOD_ELEMENT, 0.03F);
        register(context, WATER_ELEMENT, 0.04F);
        register(context, FIRE_ELEMENT, 0.06F);
    }

    static void register(BootstrapContext<DamageType> context, ResourceKey<DamageType> key, float exhaustion){
        context.register(key, new DamageType("imm." + key.location().getPath(), DamageScaling.NEVER, exhaustion, DamageEffects.HURT, DeathMessageType.DEFAULT));
    }

    static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Util.prefix(name));
    }
}
