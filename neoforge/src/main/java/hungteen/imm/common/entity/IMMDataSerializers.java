package hungteen.imm.common.entity;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.api.registry.SimpleEntry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.imm.IMMInitializer;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.setting.HumanSetting;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.impl.registry.SectTypes;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.util.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-17 12:06
 **/
public interface IMMDataSerializers {

    HTVanillaRegistry<EntityDataSerializer<?>> DATA_SERIALIZERS = HTRegistryManager.vanilla(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Util.id());

    HTHolder<EntityDataSerializer<RealmType>> REALM = DATA_SERIALIZERS.register("realm", () -> new SimpleEntityDataSerializer<>(RealmTypes.registry()));
    HTHolder<EntityDataSerializer<SpellType>> SPELL = DATA_SERIALIZERS.register("spell", () -> new SimpleEntityDataSerializer<>(SpellTypes.registry()));
    HTHolder<EntityDataSerializer<Optional<SpellType>>> OPT_SPELL = DATA_SERIALIZERS.register("opt_spell", () -> new OptionalEntityDataSerializer<>(IMMDataSerializers.SPELL));

    HTHolder<EntityDataSerializer<ISectType>> SECT = DATA_SERIALIZERS.register("sect", () -> new SimpleEntityDataSerializer<>(SectTypes.registry()));
    HTHolder<EntityDataSerializer<HumanEntity.HumanSectData>> HUMAN_SECT_DATA = DATA_SERIALIZERS.register("human_sect_data", () -> new CodecEntityDataSerializer<>("HumanSectData", HumanEntity.HumanSectData.CODEC));

    HTHolder<EntityDataSerializer<HumanSetting>> HUMAN_SETTING = DATA_SERIALIZERS.register("human_setting", () -> new CodecEntityDataSerializer<>("HumanSetting", HumanSetting.CODEC));

    /**
     * {@link IMMInitializer#defferRegister(IEventBus)}
     */
    static void initialize(IEventBus event){
        NeoHelper.initRegistry(DATA_SERIALIZERS, event);
    }

    class OptionalEntityDataSerializer<T> implements EntityDataSerializer.ForValueType<Optional<T>> {

        public final StreamCodec<RegistryFriendlyByteBuf, Optional<T>> STREAM_CODEC = StreamCodec.of(this::write, this::read);
        private final Supplier<EntityDataSerializer<T>> serializer;

        public OptionalEntityDataSerializer(Supplier<EntityDataSerializer<T>> serializer) {
            this.serializer = serializer;
        }

        public void write(RegistryFriendlyByteBuf buf, Optional<T> opt) {
            if(opt.isPresent()){
                buf.writeBoolean(true);
                serializer.get().codec().encode(buf, opt.get());
            } else {
                buf.writeBoolean(false);
            }
        }

        public Optional<T> read(RegistryFriendlyByteBuf buf) {
            if(buf.readBoolean()){
                return Optional.of(serializer.get().codec().decode(buf));
            }
            return Optional.empty();
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, Optional<T>> codec() {
            return STREAM_CODEC;
        }
    }

    /**
     * TODO HTLIB
     */
    class CodecEntityDataSerializer<T> implements EntityDataSerializer.ForValueType<T> {

        public final StreamCodec<RegistryFriendlyByteBuf, T> STREAM_CODEC = StreamCodec.of(this::write, this::read);
        private final String name;
        private final Codec<T> codec;

        public CodecEntityDataSerializer(String name, Codec<T> codec) {
            this.name = name;
            this.codec = codec;
        }

        public void write(FriendlyByteBuf byteBuf, T entry) {
            CompoundTag tag = new CompoundTag();
            CodecHelper.encodeNbt(codec, entry).result().ifPresent(nbt -> tag.put(name(), nbt));
            byteBuf.writeNbt(tag);
        }

        public T read(FriendlyByteBuf byteBuf) {
            CompoundTag tag = byteBuf.readNbt();
            AtomicReference<T> entry = new AtomicReference<>();
            if(tag != null && tag.contains(name())){
                CodecHelper.parse(codec, tag.get(name())).result().ifPresent(entry::set);
            }
            return entry.get();
        }

        public String name(){
            return name;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, T> codec() {
            return STREAM_CODEC;
        }
    }

    class SimpleEntityDataSerializer<T extends SimpleEntry> extends CodecEntityDataSerializer<T> {

        public SimpleEntityDataSerializer(HTCustomRegistry<T> registry) {
            super(registry.getRegistryName().toString(), registry.byNameCodec());
        }
    }
}
