package hungteen.imm.util;

import com.mojang.serialization.Codec;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.imm.api.IMMAPI;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-06 22:48
 **/
public class NBTUtil {

    /**
     * 返回的list类型为tag。
     */
    public static ListTag list(CompoundTag nbt, String name){
        return nbt.getList(name, Tag.TAG_COMPOUND);
    }

    public static <T> void write(BiConsumer<String, T> consumer, String name, T value){
        if(value != null){
            consumer.accept(name, value);
        }
    }

    public static <T> void write(CompoundTag tag, Codec<T> codec, String name, T value){
        if(value != null){
            CodecHelper.encodeNbt(codec, value)
                    .resultOrPartial(msg -> IMMAPI.logger().error(msg))
                    .ifPresent(nbt -> tag.put(name, nbt));
        }
    }

    public static <T> void writeList(CompoundTag tag, Codec<T> codec, String name, Collection<T> value){
        write(tag, codec.listOf(), name, value.stream().toList());
    }

    public static <K, V> void writeMap(CompoundTag tag, Codec<K> keyCodec, Codec<V> valueCodec, String name, Map<K, V> value){
        write(tag, Codec.unboundedMap(keyCodec, valueCodec), name, value);
    }

    public static <T> void read(CompoundTag tag, Function<String, T> getter, String name, Consumer<T> consumer){
        if(tag.contains(name)){
            consumer.accept(getter.apply(name));
        }
    }

    public static <T> void read(CompoundTag tag, Codec<T> codec, String name, Consumer<? super T> consumer){
        if(tag.contains(name)){
            CodecHelper.parse(codec, tag.get(name))
                    .resultOrPartial(msg -> IMMAPI.logger().error(msg))
                    .ifPresent(consumer);
        }
    }

    public static <T> void readList(CompoundTag tag, Codec<T> codec, String name, Consumer<? super List<T>> consumer){
        read(tag, codec.listOf(), name, consumer);
    }

    public static <K, V> void readMap(CompoundTag tag, Codec<K> keyCodec, Codec<V> valueCodec, String name, Consumer<? super Map<K, V>> consumer){
        read(tag, Codec.unboundedMap(keyCodec, valueCodec), name, consumer);
    }

}
