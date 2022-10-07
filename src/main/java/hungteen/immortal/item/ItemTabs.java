package hungteen.immortal.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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

    public static final CreativeModeTab RUNE = new CreativeModeTab("immortal_rune") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ImmortalItems.RUNE.get());
        }
    };

//    public static final CreativeModeTab TALISMAN = new CreativeModeTab("immortal_talisman") {
//        @Override
//        public ItemStack makeIcon() {
//            return null;
//        }
//    };


}
