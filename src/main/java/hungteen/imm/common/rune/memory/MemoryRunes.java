package hungteen.imm.common.rune.memory;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.ImmortalMod;
import hungteen.imm.util.Util;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-02 20:38
 **/
public class MemoryRunes {

    private static final HTSimpleRegistry<IMemoryRune> MEMORY_RUNES = HTRegistryManager.create(Util.prefix("memory_runes"));

    private static final List<IMemoryRune> TYPES = new ArrayList<>();

    public static final IMemoryRune NEAREST_LIVINGS = new MemoryRune("nearest_livings", () -> MemoryModuleType.NEAREST_LIVING_ENTITIES);
    public static final IMemoryRune NEAREST_VISIBLE_LIVINGS = new MemoryRune("nearest_visible_livings", () -> MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);

    public static final IMemoryRune LOOK_TARGET = new MemoryRune("look_target", () -> MemoryModuleType.LOOK_TARGET);

    public static IHTSimpleRegistry<IMemoryRune> registry() {
        return MEMORY_RUNES;
    }

    public record MemoryRune(String name, Supplier<MemoryModuleType<?>> supplier) implements IMemoryRune {

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
           registry().register(TYPES);
        }

        public MemoryRune(String name, Supplier<MemoryModuleType<?>> supplier){
            this.name = name;
            this.supplier = supplier;
            TYPES.add(this);
        }

        @Override
        public Supplier<MemoryModuleType<?>> getMemoryModule() {
            return supplier();
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getModID() {
            return Util.id();
        }
    }
}
