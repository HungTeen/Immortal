package hungteen.imm.common.network;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.common.impl.registry.SectTypes;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-26 18:48
 **/
public class SectRelationPacket {

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

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(SectRelationPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                Optional.ofNullable(PlayerHelper.getClientPlayer()).ifPresent(player -> {
                    SectTypes.registry().getValue(message.type).ifPresent(type -> {
                        PlayerUtil.setSectRelation(player, type, message.value);
                    });
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
