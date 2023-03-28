package hungteen.imm.common.network;

import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.ImmortalAPI;
import hungteen.imm.utils.PlayerUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-13 21:34
 **/
public class IntegerDataPacket {

    private String type;
    private int value;

    public IntegerDataPacket(IRangeNumber<Integer> data, int value) {
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

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(IntegerDataPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                Optional.ofNullable(PlayerHelper.getClientPlayer()).ifPresent(player -> {
                    ImmortalAPI.get().integerDataRegistry().flatMap(l -> l.getValue(message.type)).ifPresent(data -> {
                        PlayerUtil.setIntegerData(player, data, message.value);
                    });
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
