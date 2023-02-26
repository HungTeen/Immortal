package hungteen.immortal.common.network;

import hungteen.immortal.api.registry.ISpiritualType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-02-25 22:23
 **/
public class EntityElementPacket {

    private final int entityId;
    private final String type;
    private final int value;

    public EntityElementPacket(int entityId, ISpiritualType spiritualType, int value) {
        this.entityId = entityId;
        this.type = spiritualType.getRegistryName();
        this.value = value;
    }

    public EntityElementPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.type = buffer.readUtf();
        this.value = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeUtf(this.type);
        buffer.writeInt(this.value);
    }

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(EntityElementPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{

            });
            ctx.get().setPacketHandled(true);
        }
    }
}
