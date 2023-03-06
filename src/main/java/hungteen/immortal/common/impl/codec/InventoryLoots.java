package hungteen.immortal.common.impl.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.util.WeightList;
import hungteen.immortal.api.registry.IInventoryLootType;
import hungteen.immortal.common.impl.registry.InventoryLootTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

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

    public static IHTCodecRegistry<InventoryLoot> registry(){
        return LOOTS;
    }

    public static Optional<InventoryLoot> getInventoryLoot(IInventoryLootType type, RandomSource random){
        return new WeightList<>(LOOTS.getValues().stream().filter(l -> l.type() == type).toList(), InventoryLoot::weight).getRandomItem(random);
    }

    public static List<InventoryLoot> getInventoryLoots(IInventoryLootType type){
        return LOOTS.getValues().stream().filter(l -> l.type() == type).toList();
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
