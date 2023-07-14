package hungteen.imm.common.network;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/9 15:34
 */
public class TradeOffersPacket {

    private int entityId;
    private TradeOffers tradeOffers;

    public TradeOffersPacket(int entityId, TradeOffers tradeOffers) {
        this.entityId = entityId;
        this.tradeOffers = tradeOffers;
    }

    public TradeOffersPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.tradeOffers = TradeOffers.createFromStream(buffer);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        this.tradeOffers.writeToStream(buffer);
    }

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(TradeOffersPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                PlayerHelper.getClientPlayer().ifPresent(player -> {
                    Entity entity = player.level().getEntity(message.entityId);
                    if(entity instanceof HumanEntity human){
                        human.setTradeOffers(message.tradeOffers);
                    }
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
