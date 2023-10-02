package hungteen.imm.data.loot;

import hungteen.htlib.data.loot.HTBlockLootGen;
import hungteen.htlib.util.helper.registry.BlockHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.block.plants.IMMCropBlock;
import hungteen.imm.common.block.plants.RiceBlock;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.util.Util;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/2 14:39
 **/
public class BlockLootGen extends HTBlockLootGen {

    private final Set<Block> knownBlocks = new HashSet<>();

    public BlockLootGen() {
        super(Set.of());
    }

    @Override
    protected void generate() {
        /* Plant Blocks */
        createCropDrops(IMMBlocks.RICE.get(), IMMItems.RICE_STRAW.get(), IMMItems.RICE_SEEDS.get());
        createCropDrops(IMMBlocks.JUTE.get(), IMMItems.JUTE.get(), IMMItems.JUTE_SEEDS.get());

        /* Tree Suits */

        /* Drop Self */
        ForgeRegistries.BLOCKS.getEntries().stream().filter(entry -> {
            return Util.in(entry.getKey().location()) && ! this.contains(entry.getValue()) && entry.getValue().getLootTable() != BuiltInLootTables.EMPTY;
        }).map(Map.Entry::getValue).forEach(this::dropSelf);
    }

    protected void createCropDrops(IMMCropBlock cropBlock, Item cropItem, Item seedItem){
        createCropDrops(cropBlock, cropItem, seedItem, cropBlock.getAgeProperty(), cropBlock.getMaxAge());
    }

    protected void createCropDrops(Block cropBlock, Item cropItem, Item seedItem, IntegerProperty property, int reachAge){
        this.add(cropBlock, createCropDrops(cropBlock, cropItem, seedItem, LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, reachAge))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return this.knownBlocks;
    }

    protected boolean contains(Block block){
        return this.knownBlocks.contains(block);
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        super.add(block, builder);
        this.knownBlocks.add(block);
    }


}
