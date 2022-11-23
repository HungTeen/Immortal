package hungteen.immortal.common.network;

import hungteen.immortal.client.ClientDatas;
import hungteen.immortal.utils.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-13 21:34
 **/
public class FormationPacket {

    private final String type;
    private final boolean add;
    private final int id;

    public FormationPacket(ResourceKey<Level> resourceKey, boolean add, int id) {
        this.type = Util.toString(resourceKey);
        this.add = add;
        this.id = id;
    }

    public FormationPacket(FriendlyByteBuf buffer) {
        this.type = buffer.readUtf();
        this.add = buffer.readBoolean();
        this.id = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.type);
        buffer.writeBoolean(this.add);
        buffer.writeInt(this.id);
    }

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(FormationPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                if(ClientDatas.FormationMap.containsKey(message.type)){
                    ClientDatas.FormationMap.computeIfPresent(message.type, (s, set) -> {
                        if(message.add){
                            set.add(message.id);
                        } else{
                            set.remove(message.id);
                        }
                        return set;
                    });
                } else{
                    ClientDatas.FormationMap.computeIfAbsent(message.type, key -> Stream.of(message.id).collect(Collectors.toSet()));
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
