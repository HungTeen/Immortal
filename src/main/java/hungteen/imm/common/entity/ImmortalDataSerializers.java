package hungteen.imm.common.entity;

import hungteen.imm.api.ImmortalAPI;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.impl.codec.HumanSettings;
import hungteen.imm.utils.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-17 12:06
 **/
public class ImmortalDataSerializers {

    private static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Util.id());

    public static final RegistryObject<EntityDataSerializer<IRealmType>> REALM = DATA_SERIALIZERS.register("realm", () -> new EntityDataSerializer.ForValueType<>() {
        @Override
        public void write(FriendlyByteBuf byteBuf, IRealmType realm) {
            CompoundTag tag = new CompoundTag();
            ImmortalAPI.get().realmRegistry().ifPresent(l ->{
                l.byNameCodec().encodeStart(NbtOps.INSTANCE, realm)
                        .result().ifPresent(nbt -> tag.put("EntityRealm", nbt));
            });
            byteBuf.writeNbt(tag);
        }

        @Override
        public IRealmType read(FriendlyByteBuf byteBuf) {
            CompoundTag tag = byteBuf.readNbt();
            AtomicReference<IRealmType> realm = new AtomicReference();
            if(Objects.requireNonNull(tag).contains("EntityRealm")){
                ImmortalAPI.get().realmRegistry().ifPresent(l ->{
                    l.byNameCodec().parse(NbtOps.INSTANCE, tag.get("EntityRealm"))
                            .result().ifPresent(realm::set);
                });
            }
            return realm.get();
        }

    });

    public static final RegistryObject<EntityDataSerializer<HumanSettings.HumanSetting>> HUMAN_SETTING = DATA_SERIALIZERS.register("human_setting", () -> new EntityDataSerializer.ForValueType<>() {
        @Override
        public void write(FriendlyByteBuf byteBuf, HumanSettings.HumanSetting humanSetting) {
            CompoundTag tag = new CompoundTag();
            HumanSettings.HumanSetting.CODEC.encodeStart(NbtOps.INSTANCE, humanSetting)
                    .result().ifPresent(l -> tag.put("HumanSetting", l));
            byteBuf.writeNbt(tag);
        }

        @Override
        public HumanSettings.HumanSetting read(FriendlyByteBuf byteBuf) {
            CompoundTag tag = byteBuf.readNbt();
            AtomicReference<HumanSettings.HumanSetting> humanSetting = new AtomicReference<>();
            if(Objects.requireNonNull(tag).contains("HumanSetting")){
                HumanSettings.HumanSetting.CODEC.parse(NbtOps.INSTANCE, tag.get("HumanSetting"))
                        .result().ifPresent(humanSetting::set);
            }
            return humanSetting.get();
        }

    });

    public static final RegistryObject<EntityDataSerializer<List<HumanSettings.CommonTradeEntry>>> COMMON_TRADE_ENTRIES = DATA_SERIALIZERS.register("common_trade_entries", () -> new EntityDataSerializer.ForValueType<>() {
        @Override
        public void write(FriendlyByteBuf byteBuf, List<HumanSettings.CommonTradeEntry> entries) {
            CompoundTag tag = new CompoundTag();
            HumanSettings.CommonTradeEntry.CODEC.listOf().encodeStart(NbtOps.INSTANCE, entries)
                    .result().ifPresent(l -> tag.put("CommonTradeEntries", l));
            byteBuf.writeNbt(tag);
        }

        @Override
        public List<HumanSettings.CommonTradeEntry> read(FriendlyByteBuf byteBuf) {
            CompoundTag tag = byteBuf.readNbt();
            AtomicReference<List<HumanSettings.CommonTradeEntry>> entries = new AtomicReference<>(List.of());
            if(Objects.requireNonNull(tag).contains("CommonTradeEntries")){
                HumanSettings.CommonTradeEntry.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("CommonTradeEntries"))
                        .result().ifPresent(entries::set);
            }
            return entries.get();
        }

    });

    /**
     * {@link hungteen.imm.ImmortalMod#defferRegister(IEventBus)}
     */
    public static void register(IEventBus event){
        DATA_SERIALIZERS.register(event);
    }
}
