package hungteen.imm.common.network;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.common.ElementManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
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
    private final boolean robust;
    private final float value;

    public EntityElementPacket(int entityId, Elements elements, boolean robust, float value) {
        this.entityId = entityId;
        this.type = elements.name();
        this.robust = robust;
        this.value = value;
    }

    public EntityElementPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.type = buffer.readUtf();
        this.robust = buffer.readBoolean();
        this.value = buffer.readFloat();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeUtf(this.type);
        buffer.writeBoolean(this.robust);
        buffer.writeFloat(this.value);
    }

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(EntityElementPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                PlayerHelper.getClientPlayer().map(Entity::level).ifPresent(level -> {
                    final Entity entity = level.getEntity(message.entityId);
                    final Elements element = Elements.valueOf(message.type);
                    if(entity != null){
                        ElementManager.setElementAmount(entity, element, message.robust, message.value);
                    }
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
