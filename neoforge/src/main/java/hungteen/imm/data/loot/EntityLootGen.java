package hungteen.imm.data.loot;

import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.htlib.util.helper.impl.VanillaHelper;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class EntityLootGen extends EntityLootSubProvider {

    private final Set<EntityType<?>> knownEntities = new HashSet<>();

    public EntityLootGen(HolderLookup.Provider provider) {
        super(VanillaHelper.allFeatures(), provider);
    }

    @Override
    public void generate() {
        this.add(IMMEntities.SHARP_STAKE.get(), LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(IMMItems.SPIRITUAL_WOOD.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0F, 1F)))
//                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                        ).when(LootItemKilledByPlayerCondition.killedByPlayer())
        ));
        this.add(IMMEntities.BI_FANG.get(), LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.BLAZE_ROD)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
//                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                        ).when(LootItemKilledByPlayerCondition.killedByPlayer())
        ));

        /* No Drops */
        Util.get().filterEntries(EntityHelper.get(), JavaHelper::alwaysTrue).stream().filter(entry -> {
            return ! this.contains(entry.getValue()) && this.canHaveLootTable(entry.getValue()) && entry.getValue().getDefaultLootTable() != BuiltInLootTables.EMPTY;
        }).map(Map.Entry::getValue).forEach(type -> this.add(type, LootTable.lootTable()));
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return knownEntities.stream();
    }

    protected boolean contains(EntityType<?> type){
        return this.knownEntities.contains(type);
    }

    @Override
    protected void add(EntityType<?> type, ResourceKey<LootTable> loot, LootTable.Builder builder) {
        super.add(type, loot, builder);
        this.knownEntities.add(type);
    }
}
