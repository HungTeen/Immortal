package hungteen.imm.common.network;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.common.ElementManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
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
    private final long lastTick;

    public EntityElementPacket(int entityId, Elements elements, boolean robust, float value, long lastTick) {
        this.entityId = entityId;
        this.type = elements.name();
        this.robust = robust;
        this.value = value;
        this.lastTick = lastTick;
    }

    public EntityElementPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.type = buffer.readUtf();
        this.robust = buffer.readBoolean();
        this.value = buffer.readFloat();
        this.lastTick = buffer.readLong();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeUtf(this.type);
        buffer.writeBoolean(this.robust);
        buffer.writeFloat(this.value);
        buffer.writeLong(this.lastTick);
    }

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(EntityElementPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Optional.ofNullable(PlayerHelper.getClientPlayer()).map(Entity::getLevel).ifPresent(level -> {
                    final Entity entity = level.getEntity(message.entityId);
                    final Elements element = Elements.valueOf(message.type);
                    if(entity != null){
                        ElementManager.setEntityElement(entity, element, message.robust, message.value, message.lastTick);
                    }
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
