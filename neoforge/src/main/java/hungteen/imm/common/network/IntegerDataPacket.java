package hungteen.imm.common.network;

import hungteen.htlib.api.registry.RangeNumber;
import hungteen.htlib.common.network.ClientPacketContext;
import hungteen.htlib.common.network.packet.PlayToClientPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-13 21:34
 **/
public class IntegerDataPacket implements PlayToClientPacket {

    private String type;
    private int value;

    public IntegerDataPacket(RangeNumber<Integer> data, int value) {
        this.type = data.getRegistryName();
        this.value = value;
    }

    public IntegerDataPacket(FriendlyByteBuf buffer) {
        this.type = buffer.readUtf();
        this.value = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.type);
        buffer.writeInt(this.value);
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
//        public static void onMessage(IntegerDataPacket message, Supplier<NetworkEvent.Context> ctx) {
//            ctx.get().enqueueWork(()->{
//                PlayerHelper.getClientPlayer().ifPresent(player -> {
//                    PlayerRangeIntegers.registry().getValue(message.type).ifPresent(data -> {
//                        PlayerUtil.setIntegerData(player, data, message.value);
//                    });
//                });
//            });
//            ctx.get().setPacketHandled(true);
//        }
//    }

}
