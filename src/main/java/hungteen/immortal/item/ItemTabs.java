package hungteen.immortal.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-03 22:45
 **/
public class ItemTabs {

    public static final CreativeModeTab MATERIALS = new CreativeModeTab("immortal_materials") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack((ImmortalItems.GOURD_SEEDS.get()));
        }
    };

    public static final CreativeModeTab RUNES = new CreativeModeTab("immortal_runes") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ImmortalItems.RUNE.get());
        }
    };

    public static final CreativeModeTab ARTIFACTS = new CreativeModeTab("immortal_artifacts") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack((ImmortalItems.FLAME_GOURD.get()));
        }
    };

//    public static final CreativeModeTab TALISMAN = new CreativeModeTab("immortal_talisman") {
//        @Override
//        public ItemStack makeIcon() {
//            return null;
//        }
//    };


}
