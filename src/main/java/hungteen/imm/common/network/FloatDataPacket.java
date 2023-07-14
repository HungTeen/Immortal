package hungteen.imm.common.network;

import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-13 21:34
 **/
public class FloatDataPacket {

    private String type;
    private float value;

    public FloatDataPacket(IRangeNumber<Float> data, float value) {
        this.type = data.getRegistryName();
        this.value = value;
    }

    public FloatDataPacket(FriendlyByteBuf buffer) {
        this.type = buffer.readUtf();
        this.value = buffer.readFloat();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.type);
        buffer.writeFloat(this.value);
    }

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(FloatDataPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                PlayerHelper.getClientPlayer().ifPresent(player -> {
                    PlayerRangeFloats.registry().getValue(message.type).ifPresent(data -> {
                        PlayerUtil.setFloatData(player, data, message.value);
                    });
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
