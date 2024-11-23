package hungteen.imm.common.block;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.block.plant.HTAttachedStemBlock;
import hungteen.htlib.common.block.plant.HTSaplingBlock;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.common.registry.HTLibBlockSuits;
import hungteen.htlib.common.registry.suit.HTBlockSuit;
import hungteen.htlib.util.NeoHelper;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.IMMInitializer;
import hungteen.imm.common.block.artifacts.ElixirRoomBlock;
import hungteen.imm.common.block.artifacts.RuneWorkBench;
import hungteen.imm.common.block.artifacts.SpiritualFurnaceBlock;
import hungteen.imm.common.block.artifacts.TeleportAnchorBlock;
import hungteen.imm.common.block.plants.*;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.item.blockitem.GourdBlockItem;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-06 16:57
 **/
public interface IMMBlocks {

    HTVanillaRegistry<Block> BLOCKS = HTRegistryManager.vanilla(Registries.BLOCK, Util.id());

    /* Plant Blocks */

    HTHolder<RiceBlock> RICE = BLOCKS.register("rice", RiceBlock::new);
    HTHolder<JuteBlock> JUTE = BLOCKS.register("jute", JuteBlock::new);
    HTHolder<GourdStemBlock> GOURD_STEM = BLOCKS.register("gourd_stem", GourdStemBlock::new);
    HTHolder<Block> GOURD_SCAFFOLD = BLOCKS.register("gourd_scaffold", GourdScaffoldBlock::new);
    HTHolder<HTAttachedStemBlock> GOURD_ATTACHED_STEM = BLOCKS.register("gourd_attached_stem", AttachedGourdStemBlock::new);
//    HTHolder<Block> GANODERMA = BLOCKS.register("ganoderma", GanodermaBlock::new);
    HTBlockSuit<Block> GANODERMA = register("ganoderma", GanodermaBlock::new);

    /* Natural Blocks */

    HTBlockSuit<Block> SPIRIT_BEDROCK = register("spirit_bedrock", SpiritBedrock::new);
    HTBlockSuit<Block> CINNABAR_ORE = register("cinnabar_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));

    /* Special Blocks */

    HTBlockSuit<Block> TELEPORT_ANCHOR = register("teleport_anchor", TeleportAnchorBlock::new);
    HTBlockSuit<Block> RUNE_WORK_BENCH = register("rune_work_bench", RuneWorkBench::new);

    /* Entity Blocks */

    HTBlockSuit<Block> COPPER_FURNACE = register("copper_furnace", () -> new SpiritualFurnaceBlock(Block.Properties.ofFullCopy(Blocks.COPPER_BLOCK), RealmTypes.COMMON_ARTIFACT));
    HTBlockSuit<Block> COPPER_ELIXIR_ROOM = register("copper_elixir_room", () -> new ElixirRoomBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL), RealmTypes.COMMON_ARTIFACT));
//    public static final HTHolder<Block> COPPER_SPIRITUAL_ROOM = BLOCKS.initialize("copper_spiritual_room", () -> new SpiritualRoom(ArtifactTypes.COMMON_ARTIFACT));
//    public static final HTHolder<Block> COPPER_SMITHING_ARTIFACT = BLOCKS.initialize("copper_smithing_artifact", () -> new SmithingArtifact(ArtifactTypes.COMMON_ARTIFACT));

    /* Tree Suits */

    HTBlockSuit<Block> MULBERRY_SAPLING = register("mulberry_sapling", () -> new HTSaplingBlock(TreeGrower.BIRCH));
    HTBlockSuit<Block> MULBERRY_LEAVES = register("mulberry_leaves", () -> new LeavesBlock(Block.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    HTBlockSuit<Block> MULBERRY_LEAVES_WITH_MULBERRIES = register("mulberry_leaves_with_mulberries", () -> new LeavesBlock(Block.Properties.ofFullCopy(Blocks.OAK_LEAVES)));

    /**
     * initialize blocks.
     */
    static void registerBlocks(RegisterEvent event){
        // 注册各种葫芦方块。
        for(GourdGrownBlock.GourdTypes type : GourdGrownBlock.GourdTypes.values()){
            NeoHelper.register(event, BlockHelper.get(), Util.prefix(type.toString().toLowerCase(Locale.ROOT) + "_gourd"), () -> {
                final GourdGrownBlock block = new GourdGrownBlock(type);
                type.setGourdGrownBlock(block);
                return block;
            });
        }
        // 注册各种颜色的羊毛坐垫。
        for (DyeColor color : DyeColor.values()) {
            NeoHelper.register(event, BlockHelper.get(), WoolCushionBlock.getWoolCushionLocation(color), () -> new WoolCushionBlock(color));
        }
    }

    /**
     * initialize block items.
     */
    static void registerBlockItems(RegisterEvent event){
        // Register all gourd blocks.
        BlockUtil.getGourds().forEach(pair -> {
            NeoHelper.register(event, ItemHelper.get(), GourdGrownBlock.getGourdLocation(pair.getFirst()), () -> new GourdBlockItem(pair.getFirst(), pair.getSecond()));
        });
        // Register wool cushion of all colors.
        BlockUtil.getWoolCushions().forEach(pair -> {
            NeoHelper.register(event, ItemHelper.get(), pair.getFirst(), () -> new BlockItem(pair.getSecond(), new Item.Properties()));
        });
    }

    /**
     * {@link IMMInitializer#defferRegister(IEventBus)}
     */
    static void initialize(IEventBus event){
        NeoHelper.initRegistry(BLOCKS, event);
    }

    private static <T extends Block> HTBlockSuit<T> register(String registryName, Supplier<T> blockSupplier){
        return HTLibBlockSuits.registry().register(Util.prefix(registryName), new HTBlockSuit<>(Util.prefix(registryName), blockSupplier));
    }

    private static <T extends Block> HTBlockSuit<T> register(String registryName, Supplier<T> blockSupplier, Function<T, BlockItem> itemFactory){
        return HTLibBlockSuits.registry().register(Util.prefix(registryName), new HTBlockSuit<>(Util.prefix(registryName), blockSupplier, itemFactory));
    }
}
