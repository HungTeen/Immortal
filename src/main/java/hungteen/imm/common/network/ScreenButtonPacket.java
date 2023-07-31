package hungteen.imm.common.network;

import hungteen.imm.util.PlayerUtil;
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

    public ScreenButtonPacket(Types type) {
        this(type, 0);
    }

    public ScreenButtonPacket(Types type, int val) {
        this.id = type.ordinal();
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
                    switch (Types.values()[message.id]){
                        case SET_SPAWN_POINT -> {
                            // See SetSpawnCommand.
                            player.setRespawnPosition(player.level().dimension(), player.blockPosition().above(), 0, true, false);
                        }
                        case QUIT_MEDITATION -> PlayerUtil.quitMeditate(player);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

    public enum Types {

        QUIT_MEDITATION,

        SET_SPAWN_POINT,

        ;
    }

}
