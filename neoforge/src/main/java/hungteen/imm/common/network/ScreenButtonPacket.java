package hungteen.imm.common.network;

import hungteen.htlib.common.network.ServerPacketContext;
import hungteen.htlib.common.network.packet.PlayToServerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 15:08
 */
public class ScreenButtonPacket implements PlayToServerPacket {

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

    @Override
    public void process(ServerPacketContext serverPacketContext) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }

//    public static class Handler {
//
//        /**
//         * Only Server sync to Client.
//         */
//        public static void onMessage(ScreenButtonPacket message, Supplier<NetworkEvent.Context> ctx) {
//            ctx.get().enqueueWork(()->{
//                final ServerPlayer player = ctx.get().getSender();
//                if(player != null){
//                    switch (Types.values()[message.id]){
//                        case BREAK_THROUGH -> {
//                            RealmManager.tryBreakThrough(player);
//                        }
//                        case SET_SPAWN_POINT -> {
//                            // See SetSpawnCommand.
//                            player.setRespawnPosition(player.level().dimension(), player.blockPosition().above(), 0, true, false);
//                        }
//                        case QUIT_MEDITATION -> PlayerUtil.quitMeditate(player);
//                    }
//                }
//            });
//            ctx.get().setPacketHandled(true);
//        }
//    }

    public enum Types {

        BREAK_THROUGH,

        SET_SPAWN_POINT,

        QUIT_MEDITATION,

        ;
    }

}
