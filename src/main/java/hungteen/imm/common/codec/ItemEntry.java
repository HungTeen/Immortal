package hungteen.imm.common.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-08 23:18
 **/
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
