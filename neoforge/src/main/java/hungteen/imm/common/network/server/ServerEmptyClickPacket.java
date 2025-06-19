package hungteen.imm.common.network.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
public record ServerEmptyClickPacket(boolean leftClick, boolean mainHand) implements PlayToServerPacket {

    public static final Type<ServerEmptyClickPacket> TYPE = new Type<>(Util.prefix("empty_click"));
    public static final Codec<ServerEmptyClickPacket> CODEC = RecordCodecBuilder.<ServerEmptyClickPacket>mapCodec(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("left_click", true).forGetter(ServerEmptyClickPacket::leftClick),
            Codec.BOOL.optionalFieldOf("main_hand", true).forGetter(ServerEmptyClickPacket::mainHand)
    ).apply(instance, ServerEmptyClickPacket::new)).codec();
    public static final StreamCodec<ByteBuf, ServerEmptyClickPacket> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public ServerEmptyClickPacket(boolean leftClick, InteractionHand hand){
        this(leftClick, hand == InteractionHand.MAIN_HAND);
    }

    @Override
    public void process(ServerPacketContext context) {
        if(leftClick()){
            PlayerEventHandler.onLeftClickEmpty(context.player(), mainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
        } else {
            PlayerEventHandler.rayTrace(context.player(), mainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
