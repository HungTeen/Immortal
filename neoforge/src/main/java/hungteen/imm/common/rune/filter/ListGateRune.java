package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.impl.ItemHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-18 22:48
 **/
public abstract class ListGateRune implements IFilterRune {

    private final Info info;

    public ListGateRune(Info info) {
        this.info = info;
    }

    public Info getInfo() {
        return info;
    }

    public String getDataText() {
        if(getInfo().filters().size() >= 2){
            final MutableComponent text = getInfo().filters().get(0).getFilterText();
            for(int i = 1; i < getInfo().filters().size(); i ++){
                text.append(Component.literal(" , "));
                text.append(getInfo().filters().get(i).getFilterText());
            }
            return text.getString();
        }
        return UNKNOWN_COMPONENT.getString();
    }

    public record Info(Item item, List<IFilterRune> filters) {
        public static final Codec<Info> CODEC = RecordCodecBuilder.<Info>mapCodec(instance -> instance.group(
                ItemHelper.get().getCodec().fieldOf("item").forGetter(Info::item),
                FilterRuneTypes.getCodec().listOf().fieldOf("filters").forGetter(Info::filters)
        ).apply(instance, Info::new)).codec();
    }

}
