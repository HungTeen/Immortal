package hungteen.immortal.network;

import hungteen.htlib.interfaces.IRangeData;
import hungteen.htlib.network.SpawnParticlePacket;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.utils.PlayerUtil;
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

    public IntegerDataPacket(IRangeData<Integer> data, int value) {
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
                Optional.ofNullable(hungteen.htlib.util.PlayerUtil.getClientPlayer()).ifPresent(player -> {
                    ImmortalAPI.get().getIntegerData(message.type).ifPresent(data -> {
                        PlayerUtil.setIntegerData(player, data, message.value);
                    });
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
