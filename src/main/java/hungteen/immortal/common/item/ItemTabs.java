package hungteen.immortal.common.item;

import hungteen.immortal.common.block.ImmortalBlocks;
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

    public static final CreativeModeTab DECORATIONS = new CreativeModeTab("immortal_decorations") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack((ImmortalBlocks.MULBERRY_LEAVES.get()));
        }
    };

    public static final CreativeModeTab RUNES = new CreativeModeTab("immortal_runes") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ImmortalItems.RUNE.get());
        }
    };

    public static final CreativeModeTab SPELL_BOOKS = new CreativeModeTab("immortal_books") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack((ImmortalItems.SPELL_BOOK.get()));
        }
    };

    public static final CreativeModeTab ARTIFACTS = new CreativeModeTab("immortal_artifacts") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack((ImmortalItems.SPIRITUAL_PEARL.get()));
        }
    };

    public static final CreativeModeTab ELIXIRS = new CreativeModeTab("immortal_elixirs") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack((ImmortalItems.FOUNDATION_ELIXIR.get()));
        }
    };

//    public static final CreativeModeTab TALISMAN = new CreativeModeTab("immortal_talisman") {
//        @Override
//        public ItemStack makeIcon() {
//            return null;
//        }
//    };


}
