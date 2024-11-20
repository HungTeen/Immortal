package hungteen.imm.client;

import hungteen.imm.common.entity.human.setting.trade.TradeOffer;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.util.interfaces.Trader;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-03 13:28
 **/
public class ClientTrader implements Trader {

    private final Player source;
    private TradeOffers tradeOffers = new TradeOffers();

    public ClientTrader(Player source) {
        this.source = source;
    }

    @Override
    public void setTradingPlayer(@Nullable Player p_45307_) {

    }

    @Nullable
    @Override
    public Player getTradingPlayer() {
        return source;
    }

    @Override
    public void setTradeOffers(TradeOffers offers) {
        this.tradeOffers = offers;
    }

    @Override
    public TradeOffers getTradeOffers() {
        return this.tradeOffers;
    }

    @Override
    public void notifyTrade(TradeOffer offer) {

    }

    @Override
    public boolean isClientSide() {
        return true;
    }
}
