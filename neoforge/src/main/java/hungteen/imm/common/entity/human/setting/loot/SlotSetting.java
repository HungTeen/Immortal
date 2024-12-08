package hungteen.imm.common.entity.human.setting.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.SimpleWeightedList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-06-08 23:17
 **/
public record SlotSetting(SimpleWeightedList<ItemEntry> items) {
    public static final Codec<SlotSetting> CODEC = RecordCodecBuilder.<SlotSetting>mapCodec(instance -> instance.group(
            SimpleWeightedList.wrappedCodec(ItemEntry.CODEC).fieldOf("items").forGetter(SlotSetting::items)
    ).apply(instance, SlotSetting::new)).codec();

    public Optional<ItemEntry> getEntry(RandomSource random){
        return items.getItem(random);
    }

    public ItemStack getItem(RegistryAccess registryAccess, RandomSource random){
        return getEntry(random).map(l -> l.getItem(registryAccess, random)).orElse(ItemStack.EMPTY);
    }
}