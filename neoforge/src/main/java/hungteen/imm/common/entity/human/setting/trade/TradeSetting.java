package hungteen.imm.common.entity.human.setting.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.SimpleWeightedList;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-06-08 23:13
 * @param entryCount 有多少交易项。
 * @param different 交易项是否均不同。
 * @param tradeEntries 可选交易列表。
 **/
public record TradeSetting(IntProvider entryCount, boolean different, SimpleWeightedList<TradeEntry> tradeEntries) {

    public static final Codec<TradeSetting> CODEC = RecordCodecBuilder.<TradeSetting>mapCodec(instance -> instance.group(
            IntProvider.POSITIVE_CODEC.optionalFieldOf("entry_count", ConstantInt.of(1)).forGetter(TradeSetting::entryCount),
            Codec.BOOL.optionalFieldOf("different", true).forGetter(TradeSetting::different),
            SimpleWeightedList.wrappedCodec(TradeEntry.CODEC).fieldOf("trade_entries").forGetter(TradeSetting::tradeEntries)
    ).apply(instance, TradeSetting::new)).codec();

    /**
     * 进行一次抽取，筛选出交易列表。
     */
    public List<TradeEntry> sampleTradeEntries(RandomSource rand) {
        return tradeEntries().getItems(rand, entryCount().sample(rand), different());
    }

}
