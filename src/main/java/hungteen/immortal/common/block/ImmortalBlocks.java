package hungteen.immortal.common.block;

import hungteen.htlib.block.plants.HTAttachedStemBlock;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.common.block.plants.AttachedGourdStemBlock;
import hungteen.immortal.common.block.plants.GourdGrownBlock;
import hungteen.immortal.common.block.plants.GourdStemBlock;
import hungteen.immortal.common.item.ItemTabs;
import hungteen.immortal.utils.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
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
    public static final RegistryObject<Block> SPIRITUAL_FURNACE = BLOCKS.register("spiritual_furnace", () -> new SpiritualFurnace(1));
    public static final RegistryObject<Block> ELIXIR_ROOM = BLOCKS.register("elixir_room", () -> new ElixirRoom(1));

    /**
     * register blocks.
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerBlocks(RegisterEvent event){
        for(GourdGrownBlock.GourdTypes type : GourdGrownBlock.GourdTypes.values()){
            event.register(ForgeRegistries.BLOCKS.getRegistryKey(), Util.prefix(type.toString().toLowerCase(Locale.ROOT) + "_gourd"), () -> {
                final GourdGrownBlock block = new GourdGrownBlock(type);
                type.setGourdGrownBlock(block);
                return block;
            });
        }
    }

    /**
     * register block items.
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerBlockItems(RegisterEvent event){
        for(GourdGrownBlock.GourdTypes type : GourdGrownBlock.GourdTypes.values()){
            if(type.getGourdGrownBlock() != null){
                event.register(ForgeRegistries.ITEMS.getRegistryKey(), Util.prefix(type.toString().toLowerCase(Locale.ROOT) + "_gourd"), () -> {
                    Item item = new ItemNameBlockItem(type.getGourdGrownBlock(), new Item.Properties().tab(ItemTabs.MATERIALS));
                    type.setGourdItem(item);
                    return item;
                });
            }
        }
        Arrays.asList(
                SPIRITUAL_FURNACE, ELIXIR_ROOM
        ).forEach(block -> event.register(ForgeRegistries.ITEMS.getRegistryKey(), block.getId(), () -> new BlockItem(block.get(), new Item.Properties().tab(ItemTabs.ARTIFACTS))));

    }
}
