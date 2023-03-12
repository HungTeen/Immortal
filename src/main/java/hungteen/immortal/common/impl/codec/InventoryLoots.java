package hungteen.immortal.common.impl.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryHolder;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.util.WeightList;
import hungteen.immortal.api.registry.IInventoryLootType;
import hungteen.immortal.common.impl.registry.InventoryLootTypes;
import hungteen.immortal.utils.Util;
import net.minecraft.util.RandomSource;
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
public class InventoryLoots {

    /**
     * 不是全局数据包！
     */
    private static final HTCodecRegistry<InventoryLoot> LOOTS = HTRegistryManager.create(InventoryLoot.class, "inventory_loots", () -> InventoryLoot.CODEC);

    public static HTRegistryHolder<InventoryLoot> RICH_VANILLA = LOOTS.innerRegister(
            Util.prefix("rich_vanilla"),
            new InventoryLoot(
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
                    )
            )
    );

    public static HTRegistryHolder<InventoryLoot> NORMAL_VANILLA = LOOTS.innerRegister(
            Util.prefix("normal_vanilla"),
            new InventoryLoot(
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
                    )
            )
    );

    public static HTRegistryHolder<InventoryLoot> POOR_VANILLA = LOOTS.innerRegister(
            Util.prefix("poor_vanilla"),
            new InventoryLoot(
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
                    )
            )
    );

    public static void register(){

    }

    public static IHTCodecRegistry<InventoryLoot> registry(){
        return LOOTS;
    }

    public static Optional<InventoryLoot> getInventoryLoot(IInventoryLootType type, RandomSource random){
        return new WeightList<>(LOOTS.getValues().stream().filter(l -> l.type() == type).toList(), InventoryLoot::weight).getRandomItem(random);
    }

    public static List<InventoryLoot> getInventoryLoots(IInventoryLootType type){
        return LOOTS.getValues().stream().filter(l -> l.type() == type).toList();
    }

    public static LootEntry singleLoot(ItemEntry entry){
        return new LootEntry(List.of(Pair.of(entry, 1)));
    }

    public static LootEntry pairLoot(ItemEntry entry, ItemEntry entry2){
        return new LootEntry(List.of(Pair.of(entry, 10), Pair.of(entry2, 10)));
    }

    public record InventoryLoot(IInventoryLootType type, int weight, List<LootEntry> loots) {
        public static final Codec<InventoryLoot> CODEC = RecordCodecBuilder.<InventoryLoot>mapCodec(instance -> instance.group(
                InventoryLootTypes.registry().byNameCodec().fieldOf("loot_type").forGetter(InventoryLoot::type),
                Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("weight", 0).forGetter(InventoryLoot::weight),
                LootEntry.CODEC.listOf().fieldOf("loots").forGetter(InventoryLoot::loots)
        ).apply(instance, InventoryLoot::new)).codec();

        public void fill(Container container, RandomSource random){
            final int len = Math.min(container.getContainerSize(), loots().size());
            for(int i = 0; i < len; ++ i){
                container.setItem(i, loots().get(i).getItem(random));
            }
        }
    }

    public record LootEntry(List<Pair<ItemEntry, Integer>> items) {
        public static final Codec<LootEntry> CODEC = RecordCodecBuilder.<LootEntry>mapCodec(instance -> instance.group(
                Codec.mapPair(
                        ItemEntry.CODEC.fieldOf("item_entry"),
                        Codec.intRange(0, Integer.MAX_VALUE).fieldOf("weight")
                ).codec().listOf().optionalFieldOf("items", List.of()).forGetter(LootEntry::items)
        ).apply(instance, LootEntry::new)).codec();

        public Optional<ItemEntry> getEntry(RandomSource random){
            WeightList<ItemEntry> list = new WeightList(items.stream().map(Pair::getFirst).toList(), items.stream().map(Pair::getSecond).toList());
            return list.getRandomItem(random);
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

}
