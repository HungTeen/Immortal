package hungteen.imm.common.item.runes;

import net.minecraft.world.item.Item;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 18:27
 **/
public class RuneItem extends Item {

    public RuneItem() {
        super(new Properties());
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
//
//    @Override
//    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
//        super.appendHoverText(stack, level, components, tooltipFlag);
//        switch (getRuneTypes(stack)) {
//            case MEMORY -> {
//                getMemoryType(stack).ifPresent(type -> components.add(type.getComponent()));
//            }
//            case SENSOR -> {
//                getSensorType(stack).ifPresent(type -> components.add(type.getComponent()));
//            }
//            case BEHAVIOR -> {
//                getBehaviorType(stack).ifPresent(type -> {
//                    components.add(type.getComponent());
//                    type.requireMemoryStatus(level).forEach((memory, status) -> {
//                        components.add(RuneManager.getStatusText(memory, status));
//                    });
//                });
//            }
//        }
//    }

}
