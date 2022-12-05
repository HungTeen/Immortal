package hungteen.immortal.common.item;

import hungteen.htlib.util.interfaces.IBoatType;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.utils.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-11 20:47
 **/
public class ImmortalBoatTypes {

    public static final IBoatType MULBERRY = new BoatType("mulberry", ImmortalBlocks.MULBERRY_PLANKS, ImmortalItems.MULBERRY_BOAT, ImmortalItems.MULBERRY_CHEST_BOAT);

    private record BoatType(String name, Supplier<Block> plankSupplier, Supplier<Item> boatItemSupplier, Supplier<Item> chestBoatItemSupplier) implements IBoatType {
        @Override
        public Block getPlanks() {
            return plankSupplier.get();
        }

        @Override
        public Item getBoatItem() {
            return boatItemSupplier.get();
        }

        @Override
        public Item getChestBoatItem() {
            return chestBoatItemSupplier.get();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }
    }
}
