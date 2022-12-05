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

    public static final RegistryObject<Block> COPPER_SPIRITUAL_FURNACE = BLOCKS.register("copper_spiritual_furnace", () -> new SpiritualFurnace(1));
    public static final RegistryObject<Block> COPPER_ELIXIR_ROOM = BLOCKS.register("copper_elixir_room", () -> new ElixirRoom(1));
    public static final RegistryObject<Block> COPPER_SPIRITUAL_ROOM = BLOCKS.register("copper_spiritual_room", () -> new SpiritualRoom(1));
    public static final RegistryObject<Block> COPPER_SMITHING_ARTIFACT = BLOCKS.register("copper_smithing_artifact", () -> new SmithingArtifact(1));

    /* Tree Suits */

    public static final RegistryObject<Block> MULBERRY_SAPLING = BLOCKS.register("mulberry_sapling", () -> new HTSaplingBlock(new MulberryTreeGrower()));
    public static final RegistryObject<Block> MULBERRY_LEAVES = BLOCKS.register("mulberry_leaves", () -> new HTLeavesBlock(Block.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> MULBERRY_LEAVES_WITH_MULBERRIES = BLOCKS.register("mulberry_leaves_with_mulberries", () -> new HTLeavesBlock(Block.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<RotatedPillarBlock> MULBERRY_LOG = BLOCKS.register("mulberry_log", () -> new HTLogBlock(Block.Properties.copy(Blocks.OAK_LOG).strength(4.0F)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_MULBERRY_LOG = BLOCKS.register("stripped_mulberry_log", () -> new HTLogBlock(Block.Properties.copy(Blocks.STRIPPED_OAK_LOG).strength(4.0F)));
    public static final RegistryObject<RotatedPillarBlock> MULBERRY_WOOD = BLOCKS.register("mulberry_wood", () -> new HTLogBlock(Block.Properties.copy(Blocks.OAK_WOOD).strength(4.0F)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_MULBERRY_WOOD = BLOCKS.register("stripped_mulberry_wood", () -> new HTLogBlock(Block.Properties.copy(Blocks.STRIPPED_OAK_WOOD).strength(4.0F)));
    public static final RegistryObject<Block> MULBERRY_PLANKS = BLOCKS.register("mulberry_planks", () -> new HTBurnBlock(Block.Properties.copy(Blocks.OAK_PLANKS), 5, 20));
    public static final RegistryObject<DoorBlock> MULBERRY_DOOR = BLOCKS.register("mulberry_door", () -> new DoorBlock(Block.Properties.copy(Blocks.OAK_DOOR)));
    public static final RegistryObject<TrapDoorBlock> MULBERRY_TRAPDOOR = BLOCKS.register("mulberry_trapdoor", () -> new TrapDoorBlock(Block.Properties.copy(Blocks.OAK_TRAPDOOR)));
    public static final RegistryObject<FenceBlock> MULBERRY_FENCE = BLOCKS.register("mulberry_fence", () -> new HTFenceBlock(Block.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<FenceGateBlock> MULBERRY_FENCE_GATE = BLOCKS.register("mulberry_fence_gate", () -> new HTFenceGateBlock(Block.Properties.copy(Blocks.OAK_FENCE_GATE)));
    public static final RegistryObject<StandingSignBlock> MULBERRY_SIGN = BLOCKS.register("mulberry_sign", () -> new StandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD), ImmortalWoodTypes.MULBERRY));
    public static final RegistryObject<WallSignBlock> MULBERRY_WALL_SIGN = BLOCKS.register("mulberry_wall_sign", () -> new WallSignBlock(BlockBehaviour.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD), ImmortalWoodTypes.MULBERRY));
    public static final RegistryObject<StairBlock> MULBERRY_STAIRS = BLOCKS.register("mulberry_stairs", () -> new HTStairBlock(MULBERRY_PLANKS.get()));
    public static final RegistryObject<ButtonBlock> MULBERRY_BUTTON = BLOCKS.register("mulberry_button", () -> new WoodButtonBlock(Block.Properties.copy(Blocks.OAK_BUTTON)));
    public static final RegistryObject<SlabBlock> MULBERRY_SLAB = BLOCKS.register("mulberry_slab", () -> new HTSlabBlock(Block.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<PressurePlateBlock> MULBERRY_PRESSURE_PLATE = BLOCKS.register("mulberry_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.copy(Blocks.OAK_PRESSURE_PLATE)));

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
                MULBERRY_LEAVES, MULBERRY_LEAVES_WITH_MULBERRIES, MULBERRY_LOG, STRIPPED_MULBERRY_LOG, MULBERRY_WOOD, STRIPPED_MULBERRY_WOOD, MULBERRY_PLANKS, MULBERRY_DOOR, MULBERRY_TRAPDOOR, MULBERRY_FENCE, MULBERRY_FENCE_GATE, MULBERRY_STAIRS, MULBERRY_BUTTON, MULBERRY_SLAB, MULBERRY_PRESSURE_PLATE, MULBERRY_SAPLING
                ).forEach(obj -> {
                    event.register(ForgeRegistries.ITEMS.getRegistryKey(), obj.getId(), () -> new BlockItem(obj.get(), new Item.Properties().tab(ItemTabs.DECORATIONS)));
        });
        Arrays.asList(
                COPPER_SPIRITUAL_FURNACE, COPPER_ELIXIR_ROOM, COPPER_SMITHING_ARTIFACT, COPPER_SPIRITUAL_ROOM
        ).forEach(block -> event.register(ForgeRegistries.ITEMS.getRegistryKey(), block.getId(), () -> new BlockItem(block.get(), new Item.Properties().tab(ItemTabs.ARTIFACTS))));

    }
}
