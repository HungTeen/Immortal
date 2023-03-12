package hungteen.immortal.common.entity.ai;

import com.mojang.serialization.Codec;
import hungteen.immortal.utils.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-24 21:59
 **/
public class ImmortalMemories {

    private static final DeferredRegister<MemoryModuleType<?>> MEMORIES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, Util.id());

    public static final RegistryObject<MemoryModuleType<Entity>> NEAREST_BOAT = register("nearest_boat");
    public static final RegistryObject<MemoryModuleType<Boolean>> UNABLE_MELEE_ATTACK = register("unable_melee_attack");
    public static final RegistryObject<MemoryModuleType<Boolean>> UNABLE_RANGE_ATTACK = register("unable_range_attack");
    public static final RegistryObject<MemoryModuleType<Projectile>> NEAREST_PROJECTILE = register("nearest_projectile");

    private static <U> RegistryObject<MemoryModuleType<U>> register(String name){
        return MEMORIES.register(name, () -> new MemoryModuleType<>(Optional.empty()));
    }

    private static <U> RegistryObject<MemoryModuleType<U>> register(String name, Codec<U> codec){
        return MEMORIES.register(name, () -> new MemoryModuleType<>(Optional.of(codec)));
    }

    public static void register(IEventBus event){
        MEMORIES.register(event);
    }
}
