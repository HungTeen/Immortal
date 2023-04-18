package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-18 22:48
 **/
public abstract class ListGateRune implements IFilterRune {

    private final Info info;

    public ListGateRune(Info info) {
        this.info = info;
    }

    public Info getInfo() {
        return info;
    }

    public record Info(Item item, List<IFilterRune> filters) {
        public static final Codec<Info> CODEC = RecordCodecBuilder.<Info>mapCodec(instance -> instance.group(
                ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(Info::item),
                FilterRuneTypes.getCodec().listOf().fieldOf("filters").forGetter(Info::filters)
        ).apply(instance, Info::new)).codec();
    }

}
