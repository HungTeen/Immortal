package hungteen.immortal.client.particle;

import hungteen.immortal.client.ClientRegister;
import hungteen.immortal.utils.Util;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 21:56
 *
 * Step 1. make your own particle class. <br>
 * Step 2. register particle type here. <br>
 * Step 3. bind your particle factory at {@link ClientRegister#registerFactories(ParticleFactoryRegisterEvent)}
 **/
public class ImmortalParticles {

    //don't forget register particle factory in client register.
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =  DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Util.id());

    public static final RegistryObject<SimpleParticleType> IMMORTAL_FLAME = PARTICLE_TYPES.register("immortal_flame", () -> new SimpleParticleType(false));


}
