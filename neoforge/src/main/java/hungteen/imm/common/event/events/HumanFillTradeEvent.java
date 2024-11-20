package hungteen.imm.common.event.events;

import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/28 9:22
 */
public class HumanFillTradeEvent extends LivingEvent {

    private final HumanEntity human;
    private final TradeOffers tradeOffers;

    public HumanFillTradeEvent(HumanEntity entity, TradeOffers offers) {
        super(entity);
        this.human = entity;
        this.tradeOffers = offers;
    }

    public HumanEntity getHuman() {
        return human;
    }

    public TradeOffers getTradeOffers() {
        return tradeOffers;
    }

}
