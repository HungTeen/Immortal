package hungteen.imm.client.particle;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

/**
 * Step 1. make your own particle class. <br>
 * Step 2. initialize particle type here. <br>
 * Step 3. bind your particle factory.
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-07 21:56
 **/
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class IMMParticles {

    public static final HTVanillaRegistry<ParticleType<?>> TYPES =  HTRegistryManager.vanilla(Registries.PARTICLE_TYPE, Util.id());

    public static final HTHolder<SimpleParticleType> QI = TYPES.register("qi", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> METAL_ELEMENT = TYPES.register("metal_element", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> WOOD_ELEMENT = TYPES.register("wood_element", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> WATER_ELEMENT = TYPES.register("water_element", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> FIRE_ELEMENT = TYPES.register("fire_element", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> EARTH_ELEMENT = TYPES.register("earth_element", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> SPIRIT_ELEMENT = TYPES.register("spirit_element", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> SPIRITUAL_FLAME = TYPES.register("spiritual_flame", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> METAL_EXPLOSION = TYPES.register("metal_explosion", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> METAL_DAMAGE = TYPES.register("metal_damage", () -> new SimpleParticleType(false));
    public static final HTHolder<SimpleParticleType> INTIMIDATION = TYPES.register("intimidation", () -> new SimpleParticleType(true));

    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(QI.get(), QiParticle.Factory::new);
        event.registerSpriteSet(METAL_ELEMENT.get(), MetalElementParticle.Factory::new);
        event.registerSpriteSet(WOOD_ELEMENT.get(), WoodElementParticle.Factory::new);
        event.registerSpriteSet(WATER_ELEMENT.get(), WaterElementParticle.Factory::new);
        event.registerSpriteSet(FIRE_ELEMENT.get(), FireElementParticle.Factory::new);
        event.registerSpriteSet(EARTH_ELEMENT.get(), EarthElementParticle.Factory::new);
        event.registerSpriteSet(SPIRIT_ELEMENT.get(), SpiritElementParticle.Factory::new);
        event.registerSpriteSet(SPIRITUAL_FLAME.get(), IMMFlameParticle.Factory::new);
        event.registerSpriteSet(METAL_EXPLOSION.get(), MetalExplosionParticle.Factory::new);
        event.registerSpriteSet(METAL_DAMAGE.get(), MetalDamageParticle.Factory::new);

        event.registerSpecial(INTIMIDATION.get(), new IntimidationParticle.Factory());
    }
    
    public static void initialize(IEventBus event){
        NeoHelper.initRegistry(TYPES, event);
    }
}
