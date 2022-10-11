package hungteen.immortal.block;

import hungteen.htlib.block.plants.HTAttachedStemBlock;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.block.plants.AttachedGourdStemBlock;
import hungteen.immortal.block.plants.GourdGrownBlock;
import hungteen.immortal.block.plants.GourdStemBlock;
import hungteen.immortal.item.ItemTabs;
import hungteen.immortal.utils.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 16:57
 **/
public class ImmortalBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Util.id());

    /* Plant Blocks */
    public static final RegistryObject<Block> GOURD_STEM = BLOCKS.register("gourd_stem", GourdStemBlock::new);
    public static final RegistryObject<HTAttachedStemBlock> GOURD_ATTACHED_STEM = BLOCKS.register("gourd_attached_stem", AttachedGourdStemBlock::new);

    /* Entity Blocks */
    public static final RegistryObject<Block> SPIRITUAL_STOVE = BLOCKS.register("spiritual_stove", () -> new SpiritualStove(0));

    /**
     * register blocks.
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerBlocks(RegistryEvent.Register<Block> ev){
        IForgeRegistry<Block> blocks = ev.getRegistry();
        for(GourdGrownBlock.GourdTypes type : GourdGrownBlock.GourdTypes.values()){
            final GourdGrownBlock block = (GourdGrownBlock) new GourdGrownBlock(type).setRegistryName(Util.prefix(type.toString().toLowerCase(Locale.ROOT) + "_gourd"));
            type.setGourdGrownBlock(block);
            blocks.register(block);
        }
    }

    /**
     * register block items.
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerBlockItems(RegistryEvent.Register<Item> ev){
        IForgeRegistry<Item> items = ev.getRegistry();

        for(GourdGrownBlock.GourdTypes type : GourdGrownBlock.GourdTypes.values()){
            if(type.getGourdGrownBlock() != null){
                Item item = new ItemNameBlockItem(type.getGourdGrownBlock(), new Item.Properties().tab(ItemTabs.MATERIALS)).setRegistryName(Util.prefix(type.toString().toLowerCase(Locale.ROOT) + "_gourd"));
                type.setGourdItem(item);
                items.register(item);
            }
        }
//        Arrays.asList(
//                ).forEach(block -> {
//            items.register(new BlockItem(block.get(), new Item.Properties().tab(PVZItemTabs.PVZ_BLOCK)).setRegistryName(block.get().getRegistryName()));
//        });

//        items.register(new SlotMachineItem().setRegistryName(SLOT_MACHINE.get().getRegistryName()));

    }
}
