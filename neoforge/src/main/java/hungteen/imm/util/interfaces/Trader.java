package hungteen.imm.util.interfaces;

import hungteen.imm.common.entity.human.setting.trade.TradeOffer;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

/**
 * {@link net.minecraft.world.item.trading.Merchant}
 *
 * @program Immortal
 * @author HungTeen
 * @create 2023-09-03 13:24
 **/
public interface Trader {

    void setTradingPlayer(@Nullable Player p_45307_);

    @Nullable
    Player getTradingPlayer();

    void setTradeOffers(TradeOffers offers);

    TradeOffers getTradeOffers();

    void notifyTrade(TradeOffer offer);

    default SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }

    boolean isClientSide();
}
