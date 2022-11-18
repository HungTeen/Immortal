package hungteen.immortal.common.entity;

import hungteen.immortal.utils.Util;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-17 12:06
 **/
public class ImmortalDataSerializers {

    private static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.ENTITY_DATA_SERIALIZERS.get(), Util.id());

//    public static final RegistryObject<EntityDataSerializer<IRealm>> REALM = DATA_SERIALIZERS.register("realm", () -> new EntityDataSerializer.ForValueType<>() {
//        @Override
//        public void write(FriendlyByteBuf byteBuf, IRealm realm) {
//            byteBuf.writeUtf(realm.getRegistryName());
//        }
//
//        @Override
//        public IRealm read(FriendlyByteBuf byteBuf) {
//            return ImmortalAPI.get().getRealm(byteBuf.readUtf()).orElse(Realms.MORTALITY);
//        }
//
//    });

}
