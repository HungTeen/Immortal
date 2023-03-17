package hungteen.immortal.common.impl.registry;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.api.registry.ITradeType;
import hungteen.immortal.utils.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/16 11:02
 */
public class TradeTypes {

    private static final HTSimpleRegistry<ITradeType> SPIRITUAL_TYPES = HTRegistryManager.create(Util.prefix("trade_type"));
    private static final List<ITradeType> TYPES = new ArrayList<>();

    public static HTSimpleRegistry<ITradeType> registry(){
        return SPIRITUAL_TYPES;
    }

    public static final ITradeType ITEM = new TradeType("item"){
        @Override
        public void deal(Entity solder, Entity customer) {

        }
    };

    public abstract static class TradeType implements ITradeType {

        private final String name;

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            registry().register(TYPES);
        }

        public TradeType(String name) {
            this.name = name;
            TYPES.add(this);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }

    }
}
