package hungteen.imm.common.entity.human.setting.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.SimpleWeightedList;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-08 23:13
 **/
public record TradeSetting(IntProvider tradeCount, boolean different, SimpleWeightedList<TradeEntry> trades) {

    public static final Codec<TradeSetting> CODEC = RecordCodecBuilder.<TradeSetting>mapCodec(instance -> instance.group(
            IntProvider.POSITIVE_CODEC.optionalFieldOf("trade_count", ConstantInt.of(1)).forGetter(TradeSetting::tradeCount),
            Codec.BOOL.optionalFieldOf("different", true).forGetter(TradeSetting::different),
            SimpleWeightedList.wrappedCodec(TradeEntry.CODEC).fieldOf("trades").forGetter(TradeSetting::trades)
    ).apply(instance, TradeSetting::new)).codec();

    public List<TradeEntry> getTrades(RandomSource rand) {
        return trades.getItems(rand, tradeCount.sample(rand), different);
    }

}
