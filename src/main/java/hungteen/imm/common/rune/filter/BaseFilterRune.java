package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.common.item.runes.info.FilterRuneItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 23:01
 **/
public abstract class BaseFilterRune implements IFilterRune {

    private final Info info;

    public BaseFilterRune(Info info) {
        this.info = info;
    }

    @Override
    public <T> Predicate<T> getPredicate(Class<?> clazz, Predicate<T> predicate) {
        Optional<?> opt = parse();
        if(opt.isPresent()){
            if(clazz.isInstance(opt.get())){
                return obj -> check(obj, clazz.cast(opt.get()));
            }
        }
        return obj -> false;
    }

    public abstract <T> boolean check(T target, T current);

    /**
     * 得到唯一的指定值
     */
    public Optional<?> parse(){
        if(getInfo().item() instanceof FilterRuneItem<?> infoRuneItem){
            return infoRuneItem.getCodec().parse(NbtOps.INSTANCE, getInfo().tag())
                    .result();
        }
        return Optional.empty();
    }

    public Info getInfo() {
        return info;
    }

    public record Info(Item item, CompoundTag tag) {
        public static final Codec<Info> CODEC = RecordCodecBuilder.<Info>mapCodec(instance -> instance.group(
                ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(Info::item),
                CompoundTag.CODEC.fieldOf("nbt").forGetter(Info::tag)
        ).apply(instance, Info::new)).codec();
    }

}
