package hungteen.imm.data.loot;

import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.misc.IMMLoots;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.packs.VanillaChestLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/2 16:54
 **/
public class ChestLootGen implements LootTableSubProvider {

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(IMMLoots.PLAINS_TRADING_MARKET_COMMON, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(3.0F, 8.0F))
                        .add(LootItem.lootTableItem(IMMItems.GOURD_SEEDS.get()).setWeight(3)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 3.0F)
                                )))
                        .add(LootItem.lootTableItem(IMMItems.JUTE_SEEDS.get()).setWeight(3)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 3.0F)
                                )))
                        .add(LootItem.lootTableItem(Items.COPPER_INGOT).setWeight(3)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 3.0F)
                                )))
                        .add(LootItem.lootTableItem(Items.BREAD).setWeight(7)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(2.0F, 4.0F)
                                )))
                        .add(LootItem.lootTableItem(Items.BAKED_POTATO).setWeight(7)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 3.0F)
                                )))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 4.0F)
                                )))
                        .add(LootItem.lootTableItem(IMMItems.FLOWING_CLOUD_PATTERN.get()).setWeight(3)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1F)
                                )))
                        .add(LootItem.lootTableItem(IMMItems.CONTINUOUS_MOUNTAIN_PATTERN.get()).setWeight(3)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1F)
                                )))
                        .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(4)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 2.0F)
                                )))
                        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(3)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1F)
                                )))
                ));
        consumer.accept(IMMLoots.PLAINS_TRADING_MARKET_RARE, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(4.0F, 8.0F))
                        .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(2)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 2.0F)
                                )))
                        .add(LootItem.lootTableItem(Items.COPPER_INGOT).setWeight(3)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 3.0F)
                                )))
                        .add(LootItem.lootTableItem(Items.BREAD).setWeight(7)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(2.0F, 4.0F)
                                )))
                        .add(LootItem.lootTableItem(Items.BAKED_POTATO).setWeight(7)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 3.0F)
                                )))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(3)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 4.0F)
                                )))
                        .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(4)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 2.0F)
                                )))
                        .add(LootItem.lootTableItem(IMMItems.RHOMBUS_PATTERN.get()).setWeight(3)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1F)
                                )))
                        .add(LootItem.lootTableItem(IMMItems.FOLDED_THUNDER_PATTERN.get()).setWeight(3)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1F)
                                )))
                        .add(LootItem.lootTableItem(IMMItems.SPIRITUAL_PEARL.get()).setWeight(1)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1F)
                                )))
                        .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(2)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 2.0F)
                                )))
                        .add(LootItem.lootTableItem(Items.DIAMOND_HELMET).setWeight(1)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1F)
                                )))
                        .add(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS).setWeight(1)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1F)
                                )))
                ));
    }

}
