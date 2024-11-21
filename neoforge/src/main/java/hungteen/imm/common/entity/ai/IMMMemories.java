package hungteen.imm.common.entity.ai;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.common.entity.misc.ElementCrystal;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.IEventBus;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-24 21:59
 **/
public class IMMMemories {

    private static final HTVanillaRegistry<MemoryModuleType<?>> MEMORIES = HTRegistryManager.vanilla(Registries.MEMORY_MODULE_TYPE, Util.id());

    public static final HTHolder<MemoryModuleType<Entity>> NEAREST_BOAT = register("nearest_boat");
    public static final HTHolder<MemoryModuleType<Boolean>> UNABLE_MELEE_ATTACK = register("unable_melee_attack");
    public static final HTHolder<MemoryModuleType<Boolean>> UNABLE_RANGE_ATTACK = register("unable_range_attack");
    public static final HTHolder<MemoryModuleType<Projectile>> NEAREST_PROJECTILE = register("nearest_projectile");
    public static final HTHolder<MemoryModuleType<Boolean>> SPELL_COOLING_DOWN = register("spell_cooling_down");
    public static final HTHolder<MemoryModuleType<Boolean>> IDLE_COOLING_DOWN = register("idle_cooling_down");
    public static final HTHolder<MemoryModuleType<ElementCrystal>> ELEMENT_AMETHYST = register("element_amethyst");
    public static final HTHolder<MemoryModuleType<BlockPos>> HOME = register("home");

    private static <U> HTHolder<MemoryModuleType<U>> register(String name){
        return MEMORIES.register(name, () -> new MemoryModuleType<>(Optional.empty()));
    }

    private static <U> HTHolder<MemoryModuleType<U>> register(String name, Codec<U> codec){
        return MEMORIES.register(name, () -> new MemoryModuleType<>(Optional.of(codec)));
    }

    public static void initialize(IEventBus event){
        NeoHelper.initRegistry(MEMORIES, event);
    }
}
