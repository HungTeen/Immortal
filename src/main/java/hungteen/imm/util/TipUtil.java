package hungteen.imm.util;

import net.minecraft.ChatFormatting;
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
    public static final MutableComponent SHIFT_TO_SEE_DETAILS = info("shift_to_see_details").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);

    public static MutableComponent rune(String name, Object... objects){
        return Component.translatable("rune." + Util.id() + "." + name, objects);
    }

    public static MutableComponent info(String name, Object... objects){
        return Component.translatable("info." + Util.id() + "." + name, objects);
    }

    public static MutableComponent command(String name, Object... objects){
        return Component.translatable("command." + Util.id() + "." + name, objects);
    }

}
