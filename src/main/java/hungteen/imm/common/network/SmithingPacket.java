package hungteen.imm.common.network;

import hungteen.htlib.common.network.ServerPacketContext;
import hungteen.htlib.common.network.packet.PlayToServerPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-30 22:33
 **/
public class SmithingPacket implements PlayToServerPacket {

    private final float progress;
    private final BlockPos pos;
    private final boolean isMainHand;

    public SmithingPacket(BlockPos pos, float progress, boolean isMainHand) {
        this.pos = pos;
        this.progress = progress;
        this.isMainHand = isMainHand;
    }

    public SmithingPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.progress = buffer.readFloat();
        this.isMainHand = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeFloat(this.progress);
        buffer.writeBoolean(this.isMainHand);
    }

    @Override
    public void process(ServerPacketContext serverPacketContext) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }

//    public static class Handler {
//        public static void onMessage(SmithingPacket message, Supplier<NetworkEvent.Context> ctx) {
//            ctx.get().enqueueWork(()->{
//                final ServerPlayer player = ctx.get().getSender();
////                if (player != null){
////                    BlockEntity blockEntity = player.level.getBlockEntity(message.pos);
////                    if(blockEntity instanceof SmithingArtifactBlockEntity){
////                        ((SmithingArtifactBlockEntity)blockEntity).finishSmithing(player, message.progress, message.isMainHand);
////                    }
////                }
//            });
//            ctx.get().setPacketHandled(true);
//        }
//    }

}
