package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.registry.HTSimpleRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.IMMInitializer;
import hungteen.imm.api.registry.ITradeType;
import hungteen.imm.util.Util;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/18 12:39
 */
public class TradeTypes {

    private static final HTSimpleRegistry<ITradeType> TRADE_TYPES = HTRegistryManager.simple(Util.prefix("trade_type"));
    private static final List<ITradeType> TYPES = new ArrayList<>();

    public static HTSimpleRegistry<ITradeType> registry() {
        return TRADE_TYPES;
    }

    public static final ITradeType COMMON_ITEM_TRADE = new TradeType("common_item_trade"){

        @Override
        public void openMenu(ServerPlayer player) {
        }
    };

    public abstract static class TradeType implements ITradeType {

        /**
         * {@link IMMInitializer#coreRegister()}
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
        public String name() {
            return this.name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }

    }

}
