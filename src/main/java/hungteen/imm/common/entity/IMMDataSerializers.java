package hungteen.imm.common.entity;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.api.interfaces.ISimpleEntry;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.setting.HumanSetting;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SectTypes;
import hungteen.imm.util.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-17 12:06
 **/
public class IMMDataSerializers {

    private static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Util.id());

    public static final RegistryObject<EntityDataSerializer<IRealmType>> REALM = DATA_SERIALIZERS.register("realm", () -> new SimpleEntityDataSerializer<>(RealmTypes.registry()));

    public static final RegistryObject<EntityDataSerializer<ISectType>> SECT = DATA_SERIALIZERS.register("sect", () -> new SimpleEntityDataSerializer<>(SectTypes.registry()));
    public static final RegistryObject<EntityDataSerializer<HumanEntity.HumanSectData>> HUMAN_SECT_DATA = DATA_SERIALIZERS.register("human_sect_data", () -> new CodecEntityDataSerializer<>("HumanSectData", HumanEntity.HumanSectData.CODEC));

    public static final RegistryObject<EntityDataSerializer<HumanSetting>> HUMAN_SETTING = DATA_SERIALIZERS.register("human_setting", () -> new EntityDataSerializer.ForValueType<>() {
        @Override
        public void write(FriendlyByteBuf byteBuf, HumanSetting humanSetting) {
            CompoundTag tag = new CompoundTag();
            HumanSetting.CODEC.encodeStart(NbtOps.INSTANCE, humanSetting)
                    .result().ifPresent(l -> tag.put("HumanSetting", l));
            byteBuf.writeNbt(tag);
        }

        @Override
        public HumanSetting read(FriendlyByteBuf byteBuf) {
            CompoundTag tag = byteBuf.readNbt();
            AtomicReference<HumanSetting> humanSetting = new AtomicReference<>();
            if(Objects.requireNonNull(tag).contains("HumanSetting")){
                HumanSetting.CODEC.parse(NbtOps.INSTANCE, tag.get("HumanSetting"))
                        .result().ifPresent(humanSetting::set);
            }
            return humanSetting.get();
        }

    });

    /**
     * {@link hungteen.imm.ImmortalMod#defferRegister(IEventBus)}
     */
    public static void register(IEventBus event){
        DATA_SERIALIZERS.register(event);
    }

    /**
     * TODO HTLIB
     */
    public static class CodecEntityDataSerializer<T> implements EntityDataSerializer.ForValueType<T> {

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

    public static class SimpleEntityDataSerializer<T extends ISimpleEntry> extends CodecEntityDataSerializer<T> {

        public SimpleEntityDataSerializer(IHTSimpleRegistry<T> registry) {
            super(registry.getRegistryName().toString(), registry.byNameCodec());
        }
    }
}
