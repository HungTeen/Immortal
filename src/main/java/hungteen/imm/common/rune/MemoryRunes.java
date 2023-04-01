package hungteen.imm.common.rune;

import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 18:16
 **/
public class MemoryRunes {

    private static final List<RuneManager.IMemoryRune> TYPES = new ArrayList<>();

    public static final RuneManager.IMemoryRune LOOK_TARGET = new MemoryRune("look_target",
            () -> MemoryModuleType.LOOK_TARGET
    );

    public static final RuneManager.IMemoryRune ATTACK_TARGET = new MemoryRune("attack_target",
            () -> MemoryModuleType.ATTACK_TARGET
    );

    public static final RuneManager.IMemoryRune WALK_TARGET = new MemoryRune("walk_target",
            () -> MemoryModuleType.WALK_TARGET
    );

    public static final RuneManager.IMemoryRune ATTACK_COOLING_DOWN = new MemoryRune("attack_cooling_down",
            () -> MemoryModuleType.ATTACK_COOLING_DOWN
    );

    public static final RuneManager.IMemoryRune CANT_REACH_WALK_TARGET_SINCE = new MemoryRune("cant_reach_walk_target_since",
            () -> MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE
    );

    public static final RuneManager.IMemoryRune NEAREST_VISIBLE_LIVING_ENTITIES = new MemoryRune("nearest_visible_living_entities",
            () -> MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES
    );

    public static final RuneManager.IMemoryRune PATH = new MemoryRune("path",
            () -> MemoryModuleType.PATH
    );

    public record MemoryRune(String name, Supplier<MemoryModuleType<?>> moduleTypeSupplier) implements RuneManager.IMemoryRune {

        public static void register(){
            TYPES.forEach(RuneManager::registerMemoryRune);
        }

        public MemoryRune(String name, Supplier<MemoryModuleType<?>> moduleTypeSupplier){
            this.name = name;
            this.moduleTypeSupplier = moduleTypeSupplier;
            TYPES.add(this);
        }

        @Override
        public MutableComponent getComponent() {
            return Component.translatable("rune." + getModID() + ".memory." + this.getName()).withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public Supplier<MemoryModuleType<?>> getMemoryType() {
            return this.moduleTypeSupplier;
        }
    }

}
