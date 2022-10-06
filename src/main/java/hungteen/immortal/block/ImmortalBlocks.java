package hungteen.immortal.block;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.utils.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Arrays;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 16:57
 **/
public class ImmortalBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Util.id());

    /**
     * register block items.
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerBlockItem(RegistryEvent.Register<Item> ev){
        IForgeRegistry<Item> items = ev.getRegistry();

//        Arrays.asList(
//                ).forEach(block -> {
//            items.register(new BlockItem(block.get(), new Item.Properties().tab(PVZItemTabs.PVZ_BLOCK)).setRegistryName(block.get().getRegistryName()));
//        });

//        items.register(new SlotMachineItem().setRegistryName(SLOT_MACHINE.get().getRegistryName()));

    }
}
