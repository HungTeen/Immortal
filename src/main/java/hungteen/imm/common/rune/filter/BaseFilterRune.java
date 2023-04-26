package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.imm.common.item.runes.filter.FilterRuneItem;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
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

    protected static final String INFO = "info";
    private static final String NBT = "nbt";
    private static final String ITEM = "item";
    private static final String DATA = "data";
    private final Info info;

    public BaseFilterRune(Info info) {
        this.info = info;
    }

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
            return CodecHelper.parse(infoRuneItem.getCodec(), unWarp(getInfo().tag())).result();
        }
        return Optional.empty();
    }

    public String getDataText(){
        return parse().map(l -> {
            if(l instanceof Item item) return item.getDescription();
            if(l instanceof Block block) return block.getName();
            if(l instanceof EntityType<?> type) return type.getDescription();
            if(l instanceof Boolean b) return TipUtil.misc(b ? "true" : "false").withStyle(b ? ChatFormatting.GREEN : ChatFormatting.RED).withStyle(ChatFormatting.BOLD);
            if(l instanceof Float f) return TipUtil.misc("percent", (int)(f * 100));
            return UNKNOWN_COMPONENT;
        }).orElse(UNKNOWN_COMPONENT).getString();
    }

    /**
     * 将Tag包装一层。
     */
    public static CompoundTag warp(Tag tag){
        final CompoundTag nbt = new CompoundTag();
        nbt.put(NBT, tag);
        return nbt;
    }

    /**
     * 拆开包装。
     */
    public static Tag unWarp(CompoundTag tag){
        return tag.get(NBT);
    }

    public static CompoundTag replace(Tag filterTag, Tag tag){
        CompoundTag result = new CompoundTag();
        if(filterTag instanceof CompoundTag nbt && nbt.contains(INFO)){
            result = nbt.copy();
            CompoundTag tmp = nbt.getCompound(INFO).copy();
            tmp.put(DATA, warp(tag));
            result.put(INFO, tmp);
        }
        return result;
    }

    public static <T> Optional<T> getData(Tag filterTag, Codec<T> codec){
        if(filterTag instanceof CompoundTag nbt && nbt.contains(INFO)){
            final CompoundTag tmp = nbt.getCompound(INFO);
            if(tmp.contains(DATA)){
                return CodecHelper.parse(codec, unWarp(tmp.getCompound(DATA))).result();
            }

        }
        return Optional.empty();
    }

    public Info getInfo() {
        return info;
    }

    public record Info(Item item, CompoundTag tag) {
        public static final Codec<Info> CODEC = RecordCodecBuilder.<Info>mapCodec(instance -> instance.group(
                ForgeRegistries.ITEMS.getCodec().fieldOf(ITEM).forGetter(Info::item),
                CompoundTag.CODEC.fieldOf(DATA).forGetter(Info::tag)
        ).apply(instance, Info::new)).codec();
    }

}
