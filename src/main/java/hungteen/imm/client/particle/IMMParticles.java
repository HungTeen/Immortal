package hungteen.imm.client.particle;

import hungteen.imm.client.ClientRegister;
import hungteen.imm.util.Util;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Step 1. make your own particle class. <br>
 * Step 2. register particle type here. <br>
 * Step 3. bind your particle factory at {@link ClientRegister#registerFactories(RegisterParticleProvidersEvent)}
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 21:56
 **/
public class IMMParticles {

    // Don't forget register particle factory in client register.
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =  DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Util.id());

    public static final RegistryObject<SimpleParticleType> SPIRITUAL_MANA = PARTICLE_TYPES.register("spiritual_mana", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> METAL_ELEMENT = PARTICLE_TYPES.register("metal_element", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> WOOD_ELEMENT = PARTICLE_TYPES.register("wood_element", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> WATER_ELEMENT = PARTICLE_TYPES.register("water_element", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> FIRE_ELEMENT = PARTICLE_TYPES.register("fire_element", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> EARTH_ELEMENT = PARTICLE_TYPES.register("earth_element", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SPIRIT_ELEMENT = PARTICLE_TYPES.register("spirit_element", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SPIRITUAL_FLAME = PARTICLE_TYPES.register("spiritual_flame", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> METAL_EXPLOSION = PARTICLE_TYPES.register("metal_explosion", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> METAL_DAMAGE = PARTICLE_TYPES.register("metal_damage", () -> new SimpleParticleType(false));

    /**
     * {@link hungteen.imm.ImmortalMod#defferRegister(IEventBus)}
     */
    public static void register(IEventBus event){
        PARTICLE_TYPES.register(event);
    }
}
