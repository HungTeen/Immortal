package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.imm.common.item.runes.info.FilterRuneItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 23:01
 **/
public abstract class BaseFilterRune implements IFilterRune {

    private static final String NBT = "nbt";
    private final Info info;

    public BaseFilterRune(Info info) {
        this.info = info;
    }

//    @Override
//    public <T> Predicate<T> getPredicate(Class<?> clazz, Predicate<T> predicate) {
//        Optional<?> opt = parse();
//        FilterRuneItem<?> item;
//        if(opt.isPresent()){
//            if(clazz.isInstance(opt.get())){
//                return obj -> check(obj, clazz.cast(opt.get()));
//            }
//        }
//        return obj -> false;
//    }

    @Override
    public <T> Predicate<T> getPredicate(FilterRuneItem<?> item, Predicate<T> predicate) {
        Optional<?> opt = parse();
        if(opt.isPresent()){
            Class<T> clazz = null;
            if(item.getClass().getGenericSuperclass() instanceof ParameterizedType pType){
                final Type type = pType.getActualTypeArguments()[0];
                if(type instanceof Class){
                    clazz = (Class<T>) type;
                } else if(type instanceof ParameterizedType tt){
                    clazz = (Class<T>) tt.getRawType();
                }
            }
            if(clazz != null){
                final Class<T> c = clazz;
                if(clazz.isInstance(opt.get())){
                    return obj -> check(obj, c.cast(opt.get()));
                }
            }
        }
        return obj -> false;
    }

    public abstract <T> boolean check(T target, T current);

    /**
     * 得到唯一的指定值
     */
    public Optional<?> parse(){
        if(getInfo().item() instanceof FilterRuneItem<?> infoRuneItem && getInfo().tag().contains(NBT)){
            return CodecHelper.parse(infoRuneItem.getCodec(), getInfo().tag().get(NBT)).result();
        }
        return Optional.empty();
    }

    public String getDataText(){
        return parse().map(l -> {
            if(l instanceof Item item) return item.getDescription();
            if(l instanceof Block block) return block.getName();
            if(l instanceof EntityType<?> type) return type.getDescription();
            return UNKNOWN_COMPONENT;
        }).orElse(UNKNOWN_COMPONENT).getString();
    }

    public static CompoundTag warp(Tag tag){
        final CompoundTag nbt = new CompoundTag();
        nbt.put(NBT, tag);
        return nbt;
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
