package hungteen.imm.common.entity.human.setting.trade;

import hungteen.imm.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/6/9 14:42
 */
public class TradeOffers extends ArrayList<TradeOffer> {

    public TradeOffers(){

    }

    /**
     * 下面字节流要用。
     */
    private TradeOffers(int len){
        super(len);
    }

    public TradeOffers(CompoundTag tag){
        ListTag list = NBTUtil.list(tag, "TradeOffers");

        for(int i = 0; i < list.size(); ++i) {
            this.add(TradeOffer.fromTag(list.getCompound(i)));
        }
    }

    @Nullable
    public TradeOffer getRecipeFor(List<ItemStack> payItems, int entryIndex) {
        if (entryIndex >= 0 && entryIndex < this.size()) {
            final TradeOffer offer = this.get(entryIndex);
            return offer.match(payItems) ? offer : null;
        } else {
            for (final TradeOffer offer : this) {
                if (offer.match(payItems)) {
                    return offer;
                }
            }
            return null;
        }
    }

    public void addToTag(CompoundTag compoundtag){
        ListTag listtag = new ListTag();
        for(int i = 0; i < this.size(); ++i) {
            TradeOffer offer = this.get(i);
            listtag.add(offer.createTag());
        }

        compoundtag.put("TradeOffers", listtag);
    }

    public void writeToStream(FriendlyByteBuf byteBuf) {
        byteBuf.writeCollection(this, (buf, offer) -> {
            offer.writeToStream(buf);
        });
    }

    public static TradeOffers createFromStream(FriendlyByteBuf byteBuf) {
        return byteBuf.readCollection(TradeOffers::new, TradeOffer::createFromStream);
    }
}
