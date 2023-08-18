package hungteen.imm.common.misc.damage;

import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-18 20:41
 **/
public interface IMMDamageTypes {

    ResourceKey<DamageType> SPIRITUAL_MANA = create("spiritual_mana");

    static void register(BootstapContext<DamageType> context) {
        register(context, SPIRITUAL_MANA, 0.05F);
    }

    static void register(BootstapContext<DamageType> context, ResourceKey<DamageType> key, float exhaustion){
        context.register(key, new DamageType("imm." + key.location().getPath(), DamageScaling.NEVER, exhaustion, DamageEffects.HURT, DeathMessageType.DEFAULT));
    }

    static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Util.prefix(name));
    }
}
