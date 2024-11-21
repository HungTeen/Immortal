package hungteen.imm.common.network;

import hungteen.htlib.common.network.ServerPacketContext;
import hungteen.htlib.common.network.packet.PlayToServerPacket;
import hungteen.imm.common.event.handler.PlayerEventHandler;
import hungteen.imm.util.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-30 22:33
 **/
public record EmptyClickPacket(boolean mainHand) implements PlayToServerPacket {

    public static final Type<EmptyClickPacket> TYPE = new Type<>(Util.prefix("empty_click"));
    public static final StreamCodec<ByteBuf, EmptyClickPacket> STREAM_CODEC = ByteBufCodecs.BOOL.map(EmptyClickPacket::new, EmptyClickPacket::mainHand);

    public EmptyClickPacket(InteractionHand hand){
        this(hand == InteractionHand.MAIN_HAND);
    }

    @Override
    public void process(ServerPacketContext context) {
        PlayerEventHandler.rayTrace(context.player(), mainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }

}
