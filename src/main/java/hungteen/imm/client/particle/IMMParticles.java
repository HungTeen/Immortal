package hungteen.imm.client.particle;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.IMMInitializer;
import hungteen.imm.client.ClientRegister;
import hungteen.imm.util.Util;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

/**
 * Step 1. make your own particle class. <br>
 * Step 2. initialize particle type here. <br>
 * Step 3. bind your particle factory at {@link ClientRegister#registerFactories(RegisterParticleProvidersEvent)}
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 21:56
 **/
public class IMMParticles {

    // Don't forget initialize particle factory in client initialize.
    private static final HTVanillaRegistry<ParticleType<?>> PARTICLE_TYPES =  HTRegistryManager.vanilla(Registries.PARTICLE_TYPE, Util.id());

    public static final HTHolder<SimpleParticleType> SPIRITUAL_MANA = PARTICLE_TYPES.register("spiritual_mana", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> METAL_ELEMENT = PARTICLE_TYPES.register("metal_element", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> WOOD_ELEMENT = PARTICLE_TYPES.register("wood_element", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> WATER_ELEMENT = PARTICLE_TYPES.register("water_element", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> FIRE_ELEMENT = PARTICLE_TYPES.register("fire_element", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> EARTH_ELEMENT = PARTICLE_TYPES.register("earth_element", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> SPIRIT_ELEMENT = PARTICLE_TYPES.register("spirit_element", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> SPIRITUAL_FLAME = PARTICLE_TYPES.register("spiritual_flame", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> METAL_EXPLOSION = PARTICLE_TYPES.register("metal_explosion", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> METAL_DAMAGE = PARTICLE_TYPES.register("metal_damage", () -> new SimpleParticleType(false));

    /**
     * {@link IMMInitializer#defferRegister(IEventBus)}
     */
    public static void initialize(IEventBus event){
        NeoHelper.initRegistry(PARTICLE_TYPES, event);
    }
}
