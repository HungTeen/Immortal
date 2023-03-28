package hungteen.imm.common.item.runes;

import hungteen.imm.common.RuneManager;
import hungteen.imm.common.item.ImmortalItemTabs;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 18:27
 **/
public class RuneItem extends Item {

    public static final String MEMORY_RUNE_TYPE = "MemoryRuneType";
    public static final String SENSOR_RUNE_TYPE = "SensorRuneType";
    public static final String BEHAVIOR_RUNE_TYPE = "BehaviorRuneType";
    private final RuneTypes type;

    public RuneItem(RuneTypes type) {
        super(new Properties());
        this.type = type;
    }

//    @Override
//    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
//        super.fillItemCategory(tab, stacks);
//        if(this.allowedIn(tab)) {
//            switch (type) {
//                case MEMORY -> {
//                    RuneManager.getMemoryRunes().forEach(type -> {
//                        ItemStack stack = new ItemStack(this);
//                        setMemoryType(stack, type);
//                        stacks.add(stack);
//                    });
//                }
//                case SENSOR -> {
//                    RuneManager.getSensorRunes().forEach(type -> {
//                        ItemStack stack = new ItemStack(this);
//                        setSensorType(stack, type);
//                        stacks.add(stack);
//                    });
//                }
//                case BEHAVIOR -> {
//                    RuneManager.getBehaviorRunes().forEach(type -> {
//                        ItemStack stack = new ItemStack(this);
//                        setBehaviorType(stack, type);
//                        stacks.add(stack);
//                    });
//                }
//            }
//        }
//    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, components, tooltipFlag);
        switch (getRuneTypes(stack)) {
            case MEMORY -> {
                getMemoryType(stack).ifPresent(type -> components.add(type.getComponent()));
            }
            case SENSOR -> {
                getSensorType(stack).ifPresent(type -> components.add(type.getComponent()));
            }
            case BEHAVIOR -> {
                getBehaviorType(stack).ifPresent(type -> {
                    components.add(type.getComponent());
                    type.requireMemoryStatus(level).forEach((memory, status) -> {
                        components.add(RuneManager.getStatusText(memory, status));
                    });
                });
            }
        }
    }

    public static void setMemoryType(ItemStack stack, RuneManager.IMemoryRune memoryRune){
        if(getRuneTypes(stack) == RuneTypes.MEMORY){
            stack.getOrCreateTag().putString(MEMORY_RUNE_TYPE, memoryRune.getRegistryName());
        }
    }

    public static Optional<RuneManager.IMemoryRune> getMemoryType(ItemStack stack){
        if(getRuneTypes(stack) == RuneTypes.MEMORY && stack.getOrCreateTag().contains(MEMORY_RUNE_TYPE)){
            return RuneManager.getMemoryRune(stack.getOrCreateTag().getString(MEMORY_RUNE_TYPE));
        }
        return Optional.empty();
    }

    public static void setSensorType(ItemStack stack, RuneManager.ISensorRune sensorRune){
        if(getRuneTypes(stack) == RuneTypes.SENSOR){
            stack.getOrCreateTag().putString(SENSOR_RUNE_TYPE, sensorRune.getRegistryName());
        }
    }

    public static Optional<RuneManager.ISensorRune> getSensorType(ItemStack stack){
        if(getRuneTypes(stack) == RuneTypes.SENSOR && stack.getOrCreateTag().contains(SENSOR_RUNE_TYPE)){
            return RuneManager.getSensorRune(stack.getOrCreateTag().getString(SENSOR_RUNE_TYPE));
        }
        return Optional.empty();
    }

    public static void setBehaviorType(ItemStack stack, RuneManager.IBehaviorRune memoryRune){
        if(getRuneTypes(stack) == RuneTypes.BEHAVIOR){
            stack.getOrCreateTag().putString(BEHAVIOR_RUNE_TYPE, memoryRune.getRegistryName());
        }
    }

    public static Optional<RuneManager.IBehaviorRune> getBehaviorType(ItemStack stack){
        if(getRuneTypes(stack) == RuneTypes.BEHAVIOR && stack.getOrCreateTag().contains(BEHAVIOR_RUNE_TYPE)){
            return RuneManager.getBehaviorRune(stack.getOrCreateTag().getString(BEHAVIOR_RUNE_TYPE));
        }
        return Optional.empty();
    }

    public static RuneTypes getRuneTypes(ItemStack stack){
        return stack.getItem() instanceof RuneItem ? ((RuneItem) stack.getItem()).getRuneTypes() : RuneTypes.DEFAULT;
    }

    public RuneTypes getRuneTypes(){
        return this.type;
    }

    public enum RuneTypes {

        DEFAULT,
        MEMORY,
        SENSOR,
        BEHAVIOR

    }

}
