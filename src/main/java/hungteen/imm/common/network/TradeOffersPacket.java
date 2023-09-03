package hungteen.imm.common.network;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.common.menu.MerchantTradeMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/9 15:34
 */
public class TradeOffersPacket {

    private int containerId;
    private TradeOffers tradeOffers;

    public TradeOffersPacket(int containerId, TradeOffers tradeOffers) {
        this.containerId = containerId;
        this.tradeOffers = tradeOffers;
    }

    public TradeOffersPacket(FriendlyByteBuf buffer) {
        this.containerId = buffer.readInt();
        this.tradeOffers = TradeOffers.createFromStream(buffer);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.containerId);
        this.tradeOffers.writeToStream(buffer);
    }

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(TradeOffersPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                PlayerHelper.getClientPlayer().ifPresent(player -> {
                    if(player.containerMenu instanceof MerchantTradeMenu menu && player.containerMenu.containerId == message.containerId) {
                        menu.getTrader().setTradeOffers(message.tradeOffers);
                    }
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
