package hungteen.imm.common.network;

import hungteen.htlib.common.network.ClientPacketContext;
import hungteen.htlib.common.network.packet.PlayToClientPacket;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/9 15:34
 */
public class TradeOffersPacket implements PlayToClientPacket {

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

    @Override
    public void process(ClientPacketContext clientPacketContext) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }

//    public static class Handler {
//
//        /**
//         * Only Server sync to Client.
//         */
//        public static void onMessage(TradeOffersPacket message, Supplier<NetworkEvent.Context> ctx) {
//            ctx.get().enqueueWork(()->{
//                PlayerHelper.getClientPlayer().ifPresent(player -> {
//                    if(player.containerMenu instanceof MerchantTradeMenu menu && player.containerMenu.containerId == message.containerId) {
//                        menu.getTrader().setTradeOffers(message.tradeOffers);
//                    }
//                });
//            });
//            ctx.get().setPacketHandled(true);
//        }
//    }

}
