package hungteen.imm.common.network.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.network.ServerPacketContext;
import hungteen.htlib.common.network.packet.PlayToServerPacket;
import hungteen.imm.common.item.SecretManualItem;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-09-29 22:46
 **/
public record ServerManualPacket(boolean learned, boolean mainHand, int page) implements PlayToServerPacket {

    public static final Type<ServerManualPacket> TYPE = new Type<>(Util.prefix("server_manual"));
    public static final Codec<ServerManualPacket> CODEC = RecordCodecBuilder.<ServerManualPacket>mapCodec(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("learned", true).forGetter(ServerManualPacket::learned),
            Codec.BOOL.optionalFieldOf("main_hand", true).forGetter(ServerManualPacket::mainHand),
            Codec.INT.fieldOf("page").forGetter(ServerManualPacket::page)
    ).apply(instance, ServerManualPacket::new)).codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerManualPacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    @Override
    public void process(ServerPacketContext serverPacketContext) {
        ServerPlayer player = serverPacketContext.player();
        InteractionHand hand = mainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if(learned()){
            SecretManualItem.learnManual(player, hand, page());
        } else {
            SecretManualItem.changePage(player, hand, page());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
