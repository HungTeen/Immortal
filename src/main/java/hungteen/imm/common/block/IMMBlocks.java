package hungteen.imm.common.block;

import hungteen.htlib.common.block.plants.HTAttachedStemBlock;
import hungteen.htlib.common.block.plants.HTSaplingBlock;
import hungteen.htlib.util.helper.registry.BlockHelper;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.ImmortalMod;
import hungteen.imm.common.block.artifacts.ElixirRoomBlock;
import hungteen.imm.common.block.artifacts.RuneWorkBench;
import hungteen.imm.common.block.artifacts.SpiritualFurnaceBlock;
import hungteen.imm.common.block.artifacts.TeleportAnchorBlock;
import hungteen.imm.common.block.plants.*;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.item.blockitem.GourdBlockItem;
import hungteen.imm.common.world.feature.tree.MulberryTreeGrower;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
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
public class IMMBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Util.id());

    /* Plant Blocks */

    public static final RegistryObject<RiceBlock> RICE = BLOCKS.register("rice", RiceBlock::new);
    public static final RegistryObject<JuteBlock> JUTE = BLOCKS.register("jute", JuteBlock::new);
    public static final RegistryObject<GourdStemBlock> GOURD_STEM = BLOCKS.register("gourd_stem", GourdStemBlock::new);
    public static final RegistryObject<Block> GOURD_SCAFFOLD = BLOCKS.register("gourd_scaffold", GourdScaffoldBlock::new);
    public static final RegistryObject<HTAttachedStemBlock> GOURD_ATTACHED_STEM = BLOCKS.register("gourd_attached_stem", AttachedGourdStemBlock::new);
    public static final RegistryObject<Block> GANODERMA = BLOCKS.register("ganoderma", GanodermaBlock::new);

    /* Natural Blocks */

    public static final RegistryObject<Block> CINNABAR_ORE = BLOCKS.register("cinnabar_ore", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));

    /* Special Blocks */

    public static final RegistryObject<Block> TELEPORT_ANCHOR = BLOCKS.register("teleport_anchor", TeleportAnchorBlock::new);
    public static final RegistryObject<Block> RUNE_WORK_BENCH = BLOCKS.register("rune_work_bench", RuneWorkBench::new);

    /* Entity Blocks */

    public static final RegistryObject<Block> COPPER_FURNACE = BLOCKS.register("copper_furnace", () -> new SpiritualFurnaceBlock(Block.Properties.copy(Blocks.COPPER_BLOCK), RealmTypes.COMMON_ARTIFACT));
    public static final RegistryObject<Block> COPPER_ELIXIR_ROOM = BLOCKS.register("copper_elixir_room", () -> new ElixirRoomBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL), RealmTypes.COMMON_ARTIFACT));
//    public static final RegistryObject<Block> COPPER_SPIRITUAL_ROOM = BLOCKS.register("copper_spiritual_room", () -> new SpiritualRoom(ArtifactTypes.COMMON_ARTIFACT));
//    public static final RegistryObject<Block> COPPER_SMITHING_ARTIFACT = BLOCKS.register("copper_smithing_artifact", () -> new SmithingArtifact(ArtifactTypes.COMMON_ARTIFACT));

    /* Tree Suits */

    public static final RegistryObject<Block> MULBERRY_SAPLING = BLOCKS.register("mulberry_sapling", () -> new HTSaplingBlock(new MulberryTreeGrower()));
    public static final RegistryObject<Block> MULBERRY_LEAVES = BLOCKS.register("mulberry_leaves", () -> new LeavesBlock(Block.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> MULBERRY_LEAVES_WITH_MULBERRIES = BLOCKS.register("mulberry_leaves_with_mulberries", () -> new LeavesBlock(Block.Properties.copy(Blocks.OAK_LEAVES)));

    /**
     * register blocks.
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerBlocks(RegisterEvent event){
        // 注册各种葫芦方块。
        for(GourdGrownBlock.GourdTypes type : GourdGrownBlock.GourdTypes.values()){
            event.register(ForgeRegistries.BLOCKS.getRegistryKey(), Util.prefix(type.toString().toLowerCase(Locale.ROOT) + "_gourd"), () -> {
                final GourdGrownBlock block = new GourdGrownBlock(type);
                type.setGourdGrownBlock(block);
                return block;
            });
        }
        // 注册各种颜色的羊毛坐垫。
        for (DyeColor color : DyeColor.values()) {
            BlockHelper.get().register(event, WoolCushionBlock.getWoolCushionLocation(color), () -> new WoolCushionBlock(color));
        }
    }

    /**
     * register block items.
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerBlockItems(RegisterEvent event){
        // Register all gourd blocks.
        BlockUtil.getGourds().forEach(pair -> {
            ItemHelper.get().register(event, GourdGrownBlock.getGourdLocation(pair.getFirst()), () -> new GourdBlockItem(pair.getFirst(), pair.getSecond()));
        });
        // Register wool cushion of all colors.
        BlockUtil.getWoolCushions().forEach(pair -> {
            ItemHelper.get().register(event, pair.getFirst(), () -> new BlockItem(pair.getSecond(), new Item.Properties()));
        });
        Arrays.asList(
                GANODERMA,
                CINNABAR_ORE, COPPER_FURNACE, COPPER_ELIXIR_ROOM,
                TELEPORT_ANCHOR, RUNE_WORK_BENCH,
                MULBERRY_LEAVES, MULBERRY_LEAVES_WITH_MULBERRIES, MULBERRY_SAPLING
        ).forEach(obj -> {
            ItemHelper.get().register(event, obj.getId(), () -> new BlockItem(obj.get(), new Item.Properties()));
        });
//        Arrays.asList(
//                COPPER_SPIRITUAL_FURNACE, COPPER_ELIXIR_ROOM, COPPER_SMITHING_ARTIFACT, COPPER_SPIRITUAL_ROOM
//        ).forEach(block -> event.register(ForgeRegistries.ITEMS.getRegistryKey(), block.getId(), () -> new BlockItem(block.get(), new Item.Properties().tab(ItemTabs.ARTIFACTS))));

    }

    /**
     * {@link ImmortalMod#defferRegister(IEventBus)}
     */
    public static void register(IEventBus event){
        BLOCKS.register(event);
    }
}
