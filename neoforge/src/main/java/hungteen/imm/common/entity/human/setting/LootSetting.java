package hungteen.imm.common.entity.human.setting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.SimpleWeightedList;
import hungteen.imm.common.codec.ItemEntry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-08 23:17
 **/
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