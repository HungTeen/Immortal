package hungteen.imm.common.network;

import hungteen.imm.common.event.handler.PlayerEventHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-30 22:33
 **/
public class EmptyClickPacket {

    private final boolean mainHand;

    public EmptyClickPacket(InteractionHand hand) {
        this.mainHand = (hand == InteractionHand.MAIN_HAND);
    }

    public EmptyClickPacket(FriendlyByteBuf buffer) {
        this.mainHand = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.mainHand);
    }

    public static class Handler {
        public static void onMessage(EmptyClickPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                final ServerPlayer player = ctx.get().getSender();
                PlayerEventHandler.rayTrace(player, message.mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
