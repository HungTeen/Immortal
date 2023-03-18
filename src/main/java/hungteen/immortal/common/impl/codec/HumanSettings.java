package hungteen.immortal.common.impl.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryHolder;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.util.SimpleWeightedList;
import hungteen.htlib.util.WeightedList;
import hungteen.immortal.api.registry.IInventoryLootType;
import hungteen.immortal.api.registry.ITradeComponent;
import hungteen.immortal.common.entity.human.HumanEntity;
import hungteen.immortal.common.impl.codec.trades.TradeComponents;
import hungteen.immortal.common.impl.registry.InventoryLootTypes;
import hungteen.immortal.utils.Util;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/24 15:09
 */
public class HumanSettings {

    /**
     * 不是全局数据包！
     */
    private static final HTCodecRegistry<HumanSetting> LOOTS = HTRegistryManager.create(HumanSetting.class, "human_settings", () -> HumanSetting.CODEC);

    public static HTRegistryHolder<HumanSetting> RICH_VANILLA = LOOTS.innerRegister(
            Util.prefix("rich_vanilla"),
            new HumanSetting(
                    InventoryLootTypes.VANILLA,
                    100,
                    Arrays.asList(
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.PUMPKIN_PIE),
                                    UniformInt.of(16, 32),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.GOLDEN_APPLE),
                                    UniformInt.of(2, 5),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.ARROW),
                                    UniformInt.of(32, 64),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.ENDER_PEARL),
                                    UniformInt.of(2, 5),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.SHIELD),
                                    ConstantInt.of(1),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.BOW),
                                    ConstantInt.of(1),
                                    UniformInt.of(8, 15)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.DIAMOND_SWORD),
                                    ConstantInt.of(1),
                                    UniformInt.of(5, 15)
                            ), new ItemEntry(
                                    new ItemStack(Items.NETHERITE_SWORD),
                                    ConstantInt.of(1),
                                    UniformInt.of(5, 10)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.DIAMOND_CHESTPLATE),
                                    ConstantInt.of(1),
                                    UniformInt.of(5, 10)
                            ), new ItemEntry(
                                    new ItemStack(Items.NETHERITE_CHESTPLATE),
                                    ConstantInt.of(1),
                                    UniformInt.of(3, 8)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.DIAMOND_LEGGINGS),
                                    ConstantInt.of(1),
                                    UniformInt.of(5, 10)
                            ), new ItemEntry(
                                    new ItemStack(Items.NETHERITE_LEGGINGS),
                                    ConstantInt.of(1),
                                    UniformInt.of(3, 8)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.DIAMOND_HELMET),
                                    ConstantInt.of(1),
                                    UniformInt.of(8, 12)
                            ), new ItemEntry(
                                    new ItemStack(Items.IRON_HELMET),
                                    ConstantInt.of(1),
                                    UniformInt.of(10, 15)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.DIAMOND_BOOTS),
                                    ConstantInt.of(1),
                                    UniformInt.of(8, 12)
                            ), new ItemEntry(
                                    new ItemStack(Items.IRON_BOOTS),
                                    ConstantInt.of(1),
                                    UniformInt.of(10, 15)
                            ))
                    ),
                    Optional.empty()
            )
    );

    public static HTRegistryHolder<HumanSetting> NORMAL_VANILLA = LOOTS.innerRegister(
            Util.prefix("normal_vanilla"),
            new HumanSetting(
                    InventoryLootTypes.VANILLA,
                    300,
                    Arrays.asList(
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.BREAD),
                                    UniformInt.of(24, 48),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.GOLDEN_APPLE),
                                    UniformInt.of(1, 3),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.ARROW),
                                    UniformInt.of(24, 48),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.ENDER_PEARL),
                                    UniformInt.of(1, 2),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.SHIELD),
                                    ConstantInt.of(1),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.BOW),
                                    ConstantInt.of(1),
                                    UniformInt.of(4, 8)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.IRON_SWORD),
                                    ConstantInt.of(1),
                                    UniformInt.of(5, 10)
                            ), new ItemEntry(
                                    new ItemStack(Items.DIAMOND_SWORD),
                                    ConstantInt.of(1),
                                    UniformInt.of(2, 5)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.IRON_CHESTPLATE),
                                    ConstantInt.of(1),
                                    UniformInt.of(5, 10)
                            ), new ItemEntry(
                                    new ItemStack(Items.DIAMOND_CHESTPLATE),
                                    ConstantInt.of(1),
                                    UniformInt.of(2, 5)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.IRON_LEGGINGS),
                                    ConstantInt.of(1),
                                    UniformInt.of(5, 10)
                            ), new ItemEntry(
                                    new ItemStack(Items.DIAMOND_LEGGINGS),
                                    ConstantInt.of(1),
                                    UniformInt.of(2, 5)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.IRON_HELMET),
                                    ConstantInt.of(1),
                                    UniformInt.of(5, 10)
                            ), new ItemEntry(
                                    new ItemStack(Items.GOLDEN_HELMET),
                                    ConstantInt.of(1),
                                    UniformInt.of(10, 15)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.IRON_BOOTS),
                                    ConstantInt.of(1),
                                    UniformInt.of(5, 10)
                            ), new ItemEntry(
                                    new ItemStack(Items.DIAMOND_BOOTS),
                                    ConstantInt.of(1),
                                    UniformInt.of(2, 5)
                            ))
                    ),
                    Optional.empty()
            )
    );

    public static HTRegistryHolder<HumanSetting> POOR_VANILLA = LOOTS.innerRegister(
            Util.prefix("poor_vanilla"),
            new HumanSetting(
                    InventoryLootTypes.VANILLA,
                    200,
                    Arrays.asList(
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.BREAD),
                                    UniformInt.of(5, 15),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.ROTTEN_FLESH),
                                    UniformInt.of(32, 64),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.GOLDEN_APPLE),
                                    UniformInt.of(0, 1),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.ARROW),
                                    UniformInt.of(16, 32),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.ENDER_PEARL),
                                    UniformInt.of(0, 1),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.SHIELD),
                                    ConstantInt.of(1),
                                    ConstantInt.of(0)
                            )),
                            singleLoot(new ItemEntry(
                                    new ItemStack(Items.BOW),
                                    ConstantInt.of(1),
                                    UniformInt.of(0, 4)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.IRON_SWORD),
                                    ConstantInt.of(1),
                                    UniformInt.of(0, 5)
                            ), new ItemEntry(
                                    new ItemStack(Items.STONE_SWORD),
                                    ConstantInt.of(1),
                                    UniformInt.of(0, 8)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.IRON_CHESTPLATE),
                                    ConstantInt.of(1),
                                    UniformInt.of(0, 4)
                            ), new ItemEntry(
                                    new ItemStack(Items.CHAINMAIL_CHESTPLATE),
                                    ConstantInt.of(1),
                                    UniformInt.of(0, 8)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.IRON_LEGGINGS),
                                    ConstantInt.of(1),
                                    UniformInt.of(0, 5)
                            ), new ItemEntry(
                                    new ItemStack(Items.CHAINMAIL_LEGGINGS),
                                    ConstantInt.of(1),
                                    UniformInt.of(0, 8)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.CHAINMAIL_HELMET),
                                    ConstantInt.of(1),
                                    UniformInt.of(0, 8)
                            ), new ItemEntry(
                                    new ItemStack(Items.GOLDEN_HELMET),
                                    ConstantInt.of(1),
                                    UniformInt.of(0, 10)
                            )),
                            pairLoot(new ItemEntry(
                                    new ItemStack(Items.IRON_BOOTS),
                                    ConstantInt.of(1),
                                    UniformInt.of(0, 4)
                            ), new ItemEntry(
                                    new ItemStack(Items.CHAINMAIL_BOOTS),
                                    ConstantInt.of(1),
                                    UniformInt.of(0, 8)
                            ))
                    ),
                    Optional.empty()
            )
    );

    public static void register(){
    }

    public static IHTCodecRegistry<HumanSetting> registry(){
        return LOOTS;
    }

    public static Optional<HumanSetting> getHumanSetting(IInventoryLootType type, RandomSource random){
        return WeightedList.create(LOOTS.getValues().stream().filter(l -> l.type() == type).toList()).getRandomItem(random);
    }

    public static List<HumanSetting> getInventoryLoots(IInventoryLootType type){
        return LOOTS.getValues().stream().filter(l -> l.type() == type).toList();
    }

    public static LootSetting singleLoot(ItemEntry entry){
        return new LootSetting(SimpleWeightedList.single(entry));
    }

    public static LootSetting pairLoot(ItemEntry entry1, ItemEntry entry2){
        return new LootSetting(SimpleWeightedList.pair(entry1, entry2));
    }

    public record HumanSetting(IInventoryLootType type, int weight, List<LootSetting> lootSettings, Optional<TradeSetting> tradeSetting) implements WeightedEntry {
        public static final Codec<HumanSetting> CODEC = RecordCodecBuilder.<HumanSetting>mapCodec(instance -> instance.group(
                InventoryLootTypes.registry().byNameCodec().fieldOf("type").forGetter(HumanSetting::type),
                Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("weight", 0).forGetter(HumanSetting::weight),
                LootSetting.CODEC.listOf().fieldOf("loot_settings").forGetter(HumanSetting::lootSettings),
                Codec.optionalField("trade_setting", TradeSetting.CODEC).forGetter(HumanSetting::tradeSetting)
        ).apply(instance, HumanSetting::new)).codec();

        @Override
        public Weight getWeight() {
            return Weight.of(weight());
        }

        public void fillInventory(Container container, RandomSource random){
            final int len = Math.min(container.getContainerSize(), lootSettings().size());
            for(int i = 0; i < len; ++ i){
                container.setItem(i, lootSettings().get(i).getItem(random));
            }
        }

        public void fillTrade(HumanEntity entity, RandomSource random){
            tradeSetting().ifPresent(setting -> {
                entity.setTrades(setting.getTrades(random));
            });
        }
    }

    public record LootSetting(SimpleWeightedList<ItemEntry> items) {
        public static final Codec<LootSetting> CODEC = RecordCodecBuilder.<LootSetting>mapCodec(instance -> instance.group(
                SimpleWeightedList.wrappedCodec(ItemEntry.CODEC).fieldOf("items").forGetter(LootSetting::items)
        ).apply(instance, LootSetting::new)).codec();

        public Optional<ItemEntry> getEntry(RandomSource random){
            return items.getItem(random);
        }

        public ItemStack getItem(RandomSource random){
            return getEntry(random).map(l -> l.getItem(random)).orElse(ItemStack.EMPTY);
        }
    }

    public record ItemEntry(ItemStack itemStack, IntProvider count, IntProvider enchantPoint) {
        public static final Codec<ItemEntry> CODEC = RecordCodecBuilder.<ItemEntry>mapCodec(instance -> instance.group(
                ItemStack.CODEC.fieldOf("item").forGetter(ItemEntry::itemStack),
                IntProvider.codec(0, Integer.MAX_VALUE).optionalFieldOf("count", ConstantInt.of(1)).forGetter(ItemEntry::count),
                IntProvider.codec(0, Integer.MAX_VALUE).optionalFieldOf("enchant_point", ConstantInt.of(0)).forGetter(ItemEntry::enchantPoint)
        ).apply(instance, ItemEntry::new)).codec();

        public ItemStack getItem(RandomSource random){
            ItemStack stack = itemStack().copy();
            stack.setCount(count().sample(random));
            final int point = enchantPoint().sample(random);
            if(point > 0){
                EnchantmentHelper.enchantItem(random, stack, point, false);
            }
            return stack;
        }

    }

    public record TradeSetting(IntProvider tradeCount, boolean different, SimpleWeightedList<ITradeComponent> trades) {

        public static final Codec<TradeSetting> CODEC = RecordCodecBuilder.<TradeSetting>mapCodec(instance -> instance.group(
                IntProvider.POSITIVE_CODEC.optionalFieldOf("trade_count", ConstantInt.of(1)).forGetter(TradeSetting::tradeCount),
                Codec.BOOL.optionalFieldOf("different", true).forGetter(TradeSetting::different),
                SimpleWeightedList.wrappedCodec(TradeComponents.getCodec()).fieldOf("trades").forGetter(TradeSetting::trades)
        ).apply(instance, TradeSetting::new)).codec();

        public List<ITradeComponent> getTrades(RandomSource rand) {
            return trades.getItems(rand, tradeCount.sample(rand), different);
        }

    }

}
