package hungteen.imm.util;

import hungteen.htlib.util.helper.StringHelper;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.api.spell.SpellType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-03 17:08
 **/
public class TipUtil {

    public static final Function<Float, MutableComponent> PERCENT = v -> misc("percent", (int)(v * 100));
    public static final MutableComponent UNKNOWN = misc("unknown");

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

    public static MutableComponent spell(SpellType spell, int level, Object... objects){
        return Component.translatable("spell." + spell.getModID() + "." + spell.getName() + "_" + level, objects);
    }

    public static MutableComponent spell(String name, Object... objects){
        return Component.translatable("spell." + Util.id() + "." + name, objects);
    }

    public static MutableComponent misc(String name, Object... objects){
        return Component.translatable("misc." + Util.id() + "." + name, objects);
    }

    public static MutableComponent tooltip(String name, Object... objects){
        return Component.translatable("tooltip." + Util.id() + "." + name, objects);
    }

    public static MutableComponent tooltip(Item item, Object... objects){
        return tooltip(item, "", objects);
    }

    public static MutableComponent tooltip(Item item, String name, Object... objects){
        final ResourceLocation location = ItemHelper.get().getKey(item);
        return Component.translatable("tooltip." + location.getNamespace() + "." + location.getPath() + (name.isEmpty() ? "" : "." + name), objects);
    }

    public static MutableComponent desc(Item item, Object... objects){
        return desc("item", ItemHelper.get().getKey(item), objects);
    }

    public static MutableComponent desc(Block block, Object... objects){
        return desc("block", BlockHelper.get().getKey(block), objects);
    }

    public static MutableComponent desc(String category, ResourceLocation location, Object... objects){
        return StringHelper.lang(category, location.getNamespace(), location.getPath() + ".desc", objects);
    }

}
