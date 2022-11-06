package hungteen.immortal.utils;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-03 17:08
 **/
public class TipUtil {

    public static final Function<Integer, MutableComponent> SPELL_COST = cost -> new TranslatableComponent("info.immortal.spell_cost", cost);
    public static final Function<Integer, MutableComponent> SPELL_CD = cd -> new TranslatableComponent("info.immortal.spell_cd", cd);
    public static final MutableComponent ELIXIR_ROOM_TIP = new TranslatableComponent("info.immortal.elixir_room.no_furnace");


}
