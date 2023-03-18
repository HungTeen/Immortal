package hungteen.immortal.common.impl.codec.trades;

import com.mojang.serialization.Codec;
import hungteen.htlib.HTLib;
import hungteen.htlib.api.interfaces.raid.IPlaceComponent;
import hungteen.htlib.api.interfaces.raid.IPlaceComponentType;
import hungteen.htlib.common.impl.placement.AbsoluteAreaPlacement;
import hungteen.htlib.common.impl.placement.CenterAreaPlacement;
import hungteen.htlib.common.impl.placement.HTPlaceComponents;
import hungteen.htlib.common.registry.HTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryHolder;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.immortal.api.registry.ITradeComponent;
import hungteen.immortal.api.registry.ITradeType;
import hungteen.immortal.utils.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 12:39
 */
public class TradeComponents {

    public static final HTSimpleRegistry<ITradeType<?>> TRADE_TYPES = HTRegistryManager.create(StringHelper.prefix("trade_type"));
    public static final HTCodecRegistry<ITradeComponent> TRADES = HTRegistryManager.create(ITradeComponent.class, "trades", TradeComponents::getCodec, false);

    /* Trade types */

    public static final ITradeType<ItemTradeComponent> ITEM_TRADE = new DefaultTradeType<>("item_trade",  ItemTradeComponent.CODEC);
//    public static final ITradeType<AbsoluteAreaPlacement> ABSOLUTE_AREA_TYPE = new HTPlaceComponents.DefaultSpawnPlacement<>("absolute_area",  AbsoluteAreaPlacement.CODEC);

    /* Trades */

//    public static final HTRegistryHolder<IPlaceComponent> DEFAULT = PLACEMENTS.innerRegister(
//            StringHelper.prefix("default"), new CenterAreaPlacement(
//                    Vec3.ZERO, 0, 1, true, 0, true
//            )
//    );

    /**
     * {@link hungteen.immortal.ImmortalMod#setUp(FMLCommonSetupEvent)} ()}
     */
    public static void registerStuffs(){
        List.of(ITEM_TRADE).forEach(TradeComponents::registerTradeType);
    }

    public static void registerTradeType(ITradeType<?> type){
        TRADE_TYPES.register(type);
    }

    public static Codec<ITradeComponent> getCodec(){
        return TRADE_TYPES.byNameCodec().dispatch(ITradeComponent::getType, ITradeType::codec);
    }

    protected record DefaultTradeType<P extends ITradeComponent>(String name, Codec<P> codec) implements ITradeType<P> {

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
