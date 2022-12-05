package hungteen.immortal.common.entity;

import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.IRealmType;
import hungteen.immortal.utils.Util;
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

    public static void register(IEventBus event){
        DATA_SERIALIZERS.register(event);
    }
}
