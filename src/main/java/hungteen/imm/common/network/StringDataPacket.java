package hungteen.imm.common.network;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-26 18:48
 **/
public class StringDataPacket {

    private int type;
    private String data;

    public StringDataPacket(Types type, String data) {
        this.type = type.ordinal();
        this.data = data;
    }

    public StringDataPacket(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.data = buffer.readUtf();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.type);
        buffer.writeUtf(this.data);
    }

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(StringDataPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                PlayerHelper.getClientPlayer().ifPresent(player -> {
                    final Types type = Types.values()[message.type];
                    switch (type) {
                        case REALM -> {
                            IMMAPI.get().realmRegistry().ifPresent(l -> {
                                l.getValue(message.data).ifPresent(realm -> PlayerUtil.setRealm(player, realm));
                            });
                        }
                    }
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

    public enum Types{
        REALM
    }

}
