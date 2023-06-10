package hungteen.imm.common.network;

import hungteen.imm.common.menu.MerchantTradeMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 15:08
 */
public class ScreenButtonPacket {

    private final int id;
    private final int val;

    public ScreenButtonPacket(int id, int val) {
        this.id = id;
        this.val = val;
    }

    public ScreenButtonPacket(FriendlyByteBuf buffer) {
        this.id = buffer.readInt();
        this.val = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.id);
        buffer.writeInt(this.val);
    }

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(ScreenButtonPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                final ServerPlayer player = ctx.get().getSender();
                if(player != null){
                    if(player.containerMenu instanceof MerchantTradeMenu menu){

                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
