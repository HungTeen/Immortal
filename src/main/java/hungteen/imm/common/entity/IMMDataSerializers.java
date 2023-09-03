package hungteen.imm.common.entity;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.api.interfaces.ISimpleEntry;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.setting.HumanSetting;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SectTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-17 12:06
 **/
public interface IMMDataSerializers {

    DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Util.id());

    RegistryObject<EntityDataSerializer<IRealmType>> REALM = DATA_SERIALIZERS.register("realm", () -> new SimpleEntityDataSerializer<>(RealmTypes.registry()));
    RegistryObject<EntityDataSerializer<ISpellType>> SPELL = DATA_SERIALIZERS.register("spell", () -> new SimpleEntityDataSerializer<>(SpellTypes.registry()));
    RegistryObject<EntityDataSerializer<Optional<ISpellType>>> OPT_SPELL = DATA_SERIALIZERS.register("opt_spell", () -> new OptionalEntityDataSerializer<>(IMMDataSerializers.SPELL));

    RegistryObject<EntityDataSerializer<ISectType>> SECT = DATA_SERIALIZERS.register("sect", () -> new SimpleEntityDataSerializer<>(SectTypes.registry()));
    RegistryObject<EntityDataSerializer<HumanEntity.HumanSectData>> HUMAN_SECT_DATA = DATA_SERIALIZERS.register("human_sect_data", () -> new CodecEntityDataSerializer<>("HumanSectData", HumanEntity.HumanSectData.CODEC));

    RegistryObject<EntityDataSerializer<HumanSetting>> HUMAN_SETTING = DATA_SERIALIZERS.register("human_setting", () -> new CodecEntityDataSerializer<>("HumanSetting", HumanSetting.CODEC));

    /**
     * {@link hungteen.imm.ImmortalMod#defferRegister(IEventBus)}
     */
    static void register(IEventBus event){
        DATA_SERIALIZERS.register(event);
    }

    class OptionalEntityDataSerializer<T> implements EntityDataSerializer.ForValueType<Optional<T>> {

        private final Supplier<EntityDataSerializer<T>> serializer;

        public OptionalEntityDataSerializer(Supplier<EntityDataSerializer<T>> serializer) {
            this.serializer = serializer;
        }

        @Override
        public void write(FriendlyByteBuf buf, Optional<T> opt) {
            if(opt.isPresent()){
                buf.writeBoolean(true);
                serializer.get().write(buf, opt.get());
            } else {
                buf.writeBoolean(false);
            }
        }

        @Override
        public Optional<T> read(FriendlyByteBuf buf) {
            if(buf.readBoolean()){
                return Optional.of(serializer.get().read(buf));
            }
            return Optional.empty();
        }
    }

    /**
     * TODO HTLIB
     */
    class CodecEntityDataSerializer<T> implements EntityDataSerializer.ForValueType<T> {

        private final String name;
        private final Codec<T> codec;

        public CodecEntityDataSerializer(String name, Codec<T> codec) {
            this.name = name;
            this.codec = codec;
        }

        @Override
        public void write(FriendlyByteBuf byteBuf, T entry) {
            CompoundTag tag = new CompoundTag();
            CodecHelper.encodeNbt(codec(), entry).result().ifPresent(nbt -> tag.put(name(), nbt));
            byteBuf.writeNbt(tag);
        }

        @Override
        public T read(FriendlyByteBuf byteBuf) {
            CompoundTag tag = byteBuf.readNbt();
            AtomicReference<T> entry = new AtomicReference<>();
            if(tag != null && tag.contains(name())){
                CodecHelper.parse(codec(), tag.get(name())).result().ifPresent(entry::set);
            }
            return entry.get();
        }

        public String name(){
            return name;
        }

        public Codec<T> codec() {
            return codec;
        }
    }

    class SimpleEntityDataSerializer<T extends ISimpleEntry> extends CodecEntityDataSerializer<T> {

        public SimpleEntityDataSerializer(IHTSimpleRegistry<T> registry) {
            super(registry.getRegistryName().toString(), registry.byNameCodec());
        }
    }
}
