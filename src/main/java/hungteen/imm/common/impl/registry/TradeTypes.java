package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.ImmortalMod;
import hungteen.imm.api.registry.ITradeType;
import hungteen.imm.util.Util;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 12:39
 */
public class TradeTypes {

    private static final HTSimpleRegistry<ITradeType> TRADE_TYPES = HTRegistryManager.create(StringHelper.prefix("trade_type"));
    private static final List<ITradeType> TYPES = new ArrayList<>();

    public static IHTSimpleRegistry<ITradeType> registry() {
        return TRADE_TYPES;
    }

    public static final ITradeType COMMON_ITEM_TRADE = new TradeType("common_item_trade"){

        @Override
        public void openMenu(ServerPlayer player) {
        }
    };

    public abstract static class TradeType implements ITradeType {

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            TradeTypes.registry().register(TYPES);
        }

        private final String name;

        public TradeType(String name) {
            this.name = name;
            TYPES.add(this);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }

    }

}
