package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.ImmortalMod;
import hungteen.imm.util.Util;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 22:36
 **/
public class FilterRuneTypes {

    private static final HTSimpleRegistry<IFilterRuneType<?>> FILTER_RUNE_TYPES = HTRegistryManager.create(Util.prefix("filter_rune_types"));

    public static final IFilterRuneType<AndGateRune> AND = register(new DefaultRuneType<>("and", false, 2, Integer.MAX_VALUE, AndGateRune.CODEC));
    public static final IFilterRuneType<OrGateRune> OR = register(new DefaultRuneType<>("or", false, 2, Integer.MAX_VALUE, OrGateRune.CODEC));
    public static final IFilterRuneType<NotGateRune> NOT = register(new DefaultRuneType<>("not", false, 1, 1, NotGateRune.CODEC));

    public static final IFilterRuneType<EqualGateRune> EQUAL = register(new DefaultRuneType<>("equal", true, 1, 1, EqualGateRune.CODEC));
    public static final IFilterRuneType<LessGateRune> LESS = register(new DefaultRuneType<>("less", true, 1, 1, LessGateRune.CODEC));
    public static final IFilterRuneType<GreaterGateRune> GREATER = register(new DefaultRuneType<>("greater", true, 1, 1, GreaterGateRune.CODEC));

    /**
     * {@link ImmortalMod#coreRegister()}
     */
    public static void register(){
    }

    public static <T extends IFilterRune> IFilterRuneType<T> register(IFilterRuneType<T> type){
        return FILTER_RUNE_TYPES.register(type);
    }

    public static IHTSimpleRegistry<IFilterRuneType<?>> registry(){
        return FILTER_RUNE_TYPES;
    }

    public static Codec<IFilterRune> getCodec() {
        return FILTER_RUNE_TYPES.byNameCodec().dispatch(IFilterRune::getType, IFilterRuneType::codec);
    }

    private record DefaultRuneType<P extends IFilterRune>(String name, boolean isBaseType, int requireMinEntry, int requireMaxEntry, Codec<P> codec) implements IFilterRuneType<P> {

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getModID() {
            return Util.id();
        }

    }

}
