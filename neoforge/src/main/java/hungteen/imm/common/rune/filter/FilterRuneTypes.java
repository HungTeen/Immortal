package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import hungteen.htlib.api.registry.HTSimpleRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.IMMInitializer;
import hungteen.imm.util.Util;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-04 22:36
 **/
public class FilterRuneTypes {

    private static final HTSimpleRegistry<IFilterRuneType<?>> FILTER_RUNE_TYPES = HTRegistryManager.simple(Util.prefix("filter_rune_types"));

    public static final IFilterRuneType<AndGateRune> AND = register(new DefaultRuneType<>("and", false, false, 2, Integer.MAX_VALUE, AndGateRune.CODEC));
    public static final IFilterRuneType<OrGateRune> OR = register(new DefaultRuneType<>("or", false, false, 2, Integer.MAX_VALUE, OrGateRune.CODEC));
    public static final IFilterRuneType<NotGateRune> NOT = register(new DefaultRuneType<>("not", false, false, 1, 1, NotGateRune.CODEC));

    public static final IFilterRuneType<EqualGateRune> EQUAL = register(new DefaultRuneType<>("equal", true, false, 1, 1, EqualGateRune.CODEC));
    public static final IFilterRuneType<LessGateRune> LESS = register(new DefaultRuneType<>("less", true, true, 1, 1, LessGateRune.CODEC));
    public static final IFilterRuneType<GreaterGateRune> GREATER = register(new DefaultRuneType<>("greater", true, true, 1, 1, GreaterGateRune.CODEC));

    /**
     * {@link IMMInitializer#coreRegister()}
     */
    public static void register(){
    }

    public static <T extends IFilterRune> IFilterRuneType<T> register(IFilterRuneType<T> type){
        return FILTER_RUNE_TYPES.register(type);
    }

    public static HTSimpleRegistry<IFilterRuneType<?>> registry(){
        return FILTER_RUNE_TYPES;
    }

    public static Codec<IFilterRune> getCodec() {
        return FILTER_RUNE_TYPES.byNameCodec().dispatch(IFilterRune::getType, IFilterRuneType::codec);
    }

    private record DefaultRuneType<P extends IFilterRune>(String name, boolean isBaseType, boolean isNumberOperation, int requireMinEntry, int requireMaxEntry, MapCodec<P> codec) implements IFilterRuneType<P> {

        @Override
        public String getModID() {
            return Util.id();
        }

    }

}
