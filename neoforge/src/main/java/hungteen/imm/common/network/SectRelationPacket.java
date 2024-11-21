package hungteen.imm.common.network;

import hungteen.htlib.common.network.ClientPacketContext;
import hungteen.htlib.common.network.packet.PlayToClientPacket;
import hungteen.imm.api.registry.ISectType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-26 18:48
 **/
public class SectRelationPacket implements PlayToClientPacket {

    private final String type;
    private final float value;

    public SectRelationPacket(ISectType type, float value) {
        this.type = type.getRegistryName();
        this.value = value;
    }

    public SectRelationPacket(FriendlyByteBuf buffer) {
        this.type = buffer.readUtf();
        this.value = buffer.readFloat();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.type);
        buffer.writeFloat(this.value);
    }

    @Override
    public void process(ClientPacketContext clientPacketContext) {

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
//        public static void onMessage(SectRelationPacket message, Supplier<NetworkEvent.Context> ctx) {
//            ctx.get().enqueueWork(()->{
//                PlayerHelper.getClientPlayer().ifPresent(player -> {
//                    SectTypes.registry().getValue(message.type).ifPresent(type -> {
//                        PlayerUtil.setSectRelation(player, type, message.value);
//                    });
//                });
//            });
//            ctx.get().setPacketHandled(true);
//        }
//    }

}
