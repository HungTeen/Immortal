package hungteen.immortal.common.block;

import hungteen.htlib.common.block.*;
import hungteen.htlib.common.block.plants.HTAttachedStemBlock;
import hungteen.htlib.common.block.plants.HTSaplingBlock;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.common.block.artifacts.ElixirRoom;
import hungteen.immortal.common.block.artifacts.SmithingArtifact;
import hungteen.immortal.common.block.artifacts.SpiritualFurnace;
import hungteen.immortal.common.block.artifacts.SpiritualRoom;
import hungteen.immortal.common.block.plants.AttachedGourdStemBlock;
import hungteen.immortal.common.block.plants.GourdGrownBlock;
import hungteen.immortal.common.block.plants.GourdStemBlock;
import hungteen.immortal.common.impl.registry.ArtifactTypes;
import hungteen.immortal.common.item.ItemTabs;
import hungteen.immortal.common.world.feature.tree.MulberryTreeGrower;
import hungteen.immortal.utils.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
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

//    public static final RegistryObject<Block> COPPER_SPIRITUAL_FURNACE = BLOCKS.register("copper_spiritual_furnace", () -> new SpiritualFurnace(ArtifactTypes.COMMON_ARTIFACT));
//    public static final RegistryObject<Block> COPPER_ELIXIR_ROOM = BLOCKS.register("copper_elixir_room", () -> new ElixirRoom(ArtifactTypes.COMMON_ARTIFACT));
//    public static final RegistryObject<Block> COPPER_SPIRITUAL_ROOM = BLOCKS.register("copper_spiritual_room", () -> new SpiritualRoom(ArtifactTypes.COMMON_ARTIFACT));
//    public static final RegistryObject<Block> COPPER_SMITHING_ARTIFACT = BLOCKS.register("copper_smithing_artifact", () -> new SmithingArtifact(ArtifactTypes.COMMON_ARTIFACT));

    /* Tree Suits */

    public static final RegistryObject<Block> MULBERRY_SAPLING = BLOCKS.register("mulberry_sapling", () -> new HTSaplingBlock(new MulberryTreeGrower()));
    public static final RegistryObject<Block> MULBERRY_LEAVES = BLOCKS.register("mulberry_leaves", () -> new HTLeavesBlock(Block.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> MULBERRY_LEAVES_WITH_MULBERRIES = BLOCKS.register("mulberry_leaves_with_mulberries", () -> new HTLeavesBlock(Block.Properties.copy(Blocks.OAK_LEAVES)));

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
                MULBERRY_LEAVES, MULBERRY_LEAVES_WITH_MULBERRIES, MULBERRY_SAPLING
                ).forEach(obj -> {
                    event.register(ForgeRegistries.ITEMS.getRegistryKey(), obj.getId(), () -> new BlockItem(obj.get(), new Item.Properties().tab(ItemTabs.DECORATIONS)));
        });
//        Arrays.asList(
//                COPPER_SPIRITUAL_FURNACE, COPPER_ELIXIR_ROOM, COPPER_SMITHING_ARTIFACT, COPPER_SPIRITUAL_ROOM
//        ).forEach(block -> event.register(ForgeRegistries.ITEMS.getRegistryKey(), block.getId(), () -> new BlockItem(block.get(), new Item.Properties().tab(ItemTabs.ARTIFACTS))));

    }
}
