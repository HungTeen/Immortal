package hungteen.imm.data.loot;

import hungteen.htlib.common.block.HTStemBlock;
import hungteen.htlib.common.block.plant.HTCropBlock;
import hungteen.htlib.data.loot.HTBlockLootGen;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.block.plants.IMMCropBlock;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.util.Util;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/2 14:39
 **/
public class BlockLootGen extends HTBlockLootGen {

    private final Set<Block> knownBlocks = new HashSet<>();

    public BlockLootGen(HolderLookup.Provider provider) {
        super(Set.of(), provider);
    }

    @Override
    protected void generate() {
        /* Plant Blocks */
        genCropDrops(IMMBlocks.RICE.get(), IMMItems.RICE_STRAW.get(), IMMItems.RICE_SEEDS.get());
        genCropDrops(IMMBlocks.JUTE.get(), IMMItems.JUTE.get(), IMMItems.JUTE_SEEDS.get());
        genStemDrops(IMMBlocks.GOURD_STEM.get(), IMMItems.GOURD_SEEDS.get());
        genAttachedStemDrops(IMMBlocks.GOURD_ATTACHED_STEM.get(), IMMItems.GOURD_SEEDS.get());
        dropOther(IMMBlocks.GOURD_SCAFFOLD.get(), Items.SCAFFOLDING);

        /* Tree Suits */

        /* Drop Self */
        BlockHelper.get().entries().stream().filter(entry -> {
            return Util.in(entry.getKey().location()) && ! this.contains(entry.getValue()) && entry.getValue().getLootTable() != BuiltInLootTables.EMPTY;
        }).map(Map.Entry::getValue).forEach(this::dropSelf);
    }

    protected void genCropDrops(IMMCropBlock cropBlock, Item cropItem, Item seedItem){
        genCropDrops(cropBlock, cropItem, seedItem, cropBlock.getAgeProperty(), cropBlock.getMaxAge());
    }

    protected void genCropDrops(Block cropBlock, Item cropItem, Item seedItem, IntegerProperty property, int reachAge){
        this.add(cropBlock, createCropDrops(cropBlock, cropItem, seedItem, LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, reachAge))));
    }

    public void genStemDrops(HTStemBlock stem, Item seed) {
        this.add(stem, LootTable.lootTable().withPool(this.applyExplosionDecay(stem, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(seed).apply(stem.getAgeProperty().getPossibleValues(), (p_249795_) -> {
            return SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, (float)(p_249795_ + 1) / 15.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(stem).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(stem.getAgeProperty(), p_249795_)));
        })))));
    }

    public void genAttachedStemDrops(Block block, Item item) {
        this.add(block, super.createAttachedStemDrops(block, item));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return this.knownBlocks;
    }

    protected boolean contains(Block block){
        return this.knownBlocks.contains(block);
    }

    @Override
    protected void add(Block block, Function<Block, LootTable.Builder> function) {
        this.add(block, function.apply(block));
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        super.add(block, builder);
        this.knownBlocks.add(block);
    }


}
