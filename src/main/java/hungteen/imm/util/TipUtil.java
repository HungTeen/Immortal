package hungteen.imm.util;

import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.api.registry.ISpellType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-03 17:08
 **/
public class TipUtil {

    public static final Function<Integer, MutableComponent> SPELL_COST = cost -> info("spell_cost", cost);
    public static final Function<Integer, MutableComponent> SPELL_CD = cd -> info("spell_cd", cd);
    public static final Function<Float, MutableComponent> PERCENT = v -> misc("percent", (int)(v * 100));
    public static final MutableComponent SPIRITUAL_ROOT = misc("spiritual_root");
    public static final MutableComponent CULTIVATION = misc("cultivation");

    public static MutableComponent gui(String name, Object... objects){
        return Component.translatable("gui." + Util.id() + "." + name, objects);
    }

    public static MutableComponent rune(String name, Object... objects){
        return Component.translatable("rune." + Util.id() + "." + name, objects);
    }

    public static MutableComponent info(String name, Object... objects){
        return Component.translatable("info." + Util.id() + "." + name, objects);
    }

    public static MutableComponent command(String name, Object... objects){
        return Component.translatable("command." + Util.id() + "." + name, objects);
    }

    public static MutableComponent spell(ISpellType spell, int level, Object... objects){
        return Component.translatable("spell." + spell.getModID() + "." + spell.getName() + "_" + level, objects);
    }

    public static MutableComponent misc(String name, Object... objects){
        return Component.translatable("misc." + Util.id() + "." + name, objects);
    }

    public static MutableComponent tooltip(Item item, Object... objects){
        return tooltip(item, "", objects);
    }

    public static MutableComponent tooltip(Item item, String name, Object... objects){
        final ResourceLocation location = ItemHelper.get().getKey(item);
        return Component.translatable("tooltip." + location.getNamespace() + "." + location.getPath() + "." + name, objects);
    }

}
