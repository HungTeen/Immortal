package hungteen.imm.common.entity.human.setting.trade;

import hungteen.htlib.util.helper.CodecHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-08 23:12
 **/
public class TradeOffer {

    private final TradeEntry tradeEntry;
    private int tradeCount;

    public TradeOffer(TradeEntry tradeEntry, RandomSource randomSource) {
        this.tradeEntry = tradeEntry;
        this.refreshOffer(randomSource);
    }

    public TradeOffer(TradeEntry tradeEntry, int tradeCount) {
        this.tradeEntry = tradeEntry;
        this.tradeCount = tradeCount;
    }

    public CompoundTag createTag(){
        CompoundTag tag = new CompoundTag();
        CodecHelper.encodeNbt(TradeEntry.CODEC, this.getTradeEntry())
                .result().ifPresent(nbt -> tag.put("TradeEntry", nbt));
        tag.putInt("TradeCount", this.getTradeCount());
        return tag;
    }

    public static TradeOffer fromTag(CompoundTag tag){
        AtomicReference<TradeEntry> tradeEntry = new AtomicReference<>();
        if(tag.contains("TradeEntry")){
            CodecHelper.parse(TradeEntry.CODEC, tag.get("TradeEntry")).result().ifPresent(tradeEntry::set);
        }
        int tradeCount = tag.getInt("TradeCount");
        return new TradeOffer(tradeEntry.get(), tradeCount);
    }

    public void writeToStream(FriendlyByteBuf byteBuf) {
        byteBuf.writeNbt(this.createTag());
    }

    public static TradeOffer createFromStream(FriendlyByteBuf byteBuf) {
        return fromTag(Objects.requireNonNull(byteBuf.readNbt()));
    }

    public boolean match(List<ItemStack> payItems) {
        return this.tradeEntry.match(payItems);
    }

    public void refreshOffer(RandomSource randomSource) {
        this.setTradeCount(this.getTradeEntry().sampleTradeCount(randomSource));
    }

    public void consume() {
        this.setTradeCount(this.getTradeCount() - 1);
    }

    public boolean valid() {
        return this.getTradeCount() > 0;
    }

    public TradeEntry getTradeEntry() {
        return tradeEntry;
    }

    public void setTradeCount(int tradeCount) {
        this.tradeCount = tradeCount;
    }

    public int getTradeCount() {
        return tradeCount;
    }
}
