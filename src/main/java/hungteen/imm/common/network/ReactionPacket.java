package hungteen.imm.common.network;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.registry.IElementReaction;
import hungteen.imm.common.impl.registry.ElementReactions;
import hungteen.imm.util.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-23 22:24
 **/
public class ReactionPacket {

    private final int entityId;
    private final String type;

    public ReactionPacket(int entityId, IElementReaction reaction) {
        this.entityId = entityId;
        this.type = reaction.getRegistryName();
    }

    public ReactionPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.type = buffer.readUtf();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeUtf(this.type);
    }

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(ReactionPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                PlayerHelper.getClientPlayer().map(Entity::level).ifPresent(level -> {
                    final Entity entity = level.getEntity(message.entityId);
                    ElementReactions.registry().getValue(message.type).ifPresent(reaction -> {
                        Util.getProxy().addReaction(entity, reaction);
                    });
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}