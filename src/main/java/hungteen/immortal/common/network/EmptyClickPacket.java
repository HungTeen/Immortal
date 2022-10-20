package hungteen.immortal.common.network;

import hungteen.immortal.common.event.handler.PlayerEventHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-30 22:33
 **/
public class EmptyClickPacket {


    public EmptyClickPacket() {
    }

    public EmptyClickPacket(FriendlyByteBuf buffer) {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public static class Handler {
        public static void onMessage(EmptyClickPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                final ServerPlayer player = ctx.get().getSender();
                PlayerEventHandler.rayTrace(player);
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
