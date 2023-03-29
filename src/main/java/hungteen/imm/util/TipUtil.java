package hungteen.imm.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-03 17:08
 **/
public class TipUtil {

    public static final Function<Integer, MutableComponent> SPELL_COST = cost -> info("spell_cost", cost);
    public static final Function<Integer, MutableComponent> SPELL_CD = cd -> info("spell_cd", cd);
    public static final MutableComponent ELIXIR_ROOM_TIP = info("elixir_room.no_furnace");

    public static MutableComponent info(String name, Object... objects){
        return Component.translatable("info." + Util.id() + "." + name, objects);
    }

}
