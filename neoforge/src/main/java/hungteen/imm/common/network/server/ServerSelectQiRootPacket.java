package hungteen.imm.common.network.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.network.ServerPacketContext;
import hungteen.htlib.common.network.packet.PlayToServerPacket;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-09-29 22:46
 **/
public record ServerSelectQiRootPacket(List<QiRootType> roots) implements PlayToServerPacket {

    public static final Type<ServerSelectQiRootPacket> TYPE = new Type<>(Util.prefix("server_select_roots"));
    public static final Codec<ServerSelectQiRootPacket> CODEC = RecordCodecBuilder.<ServerSelectQiRootPacket>mapCodec(instance -> instance.group(
            QiRootTypes.registry().byNameCodec().listOf().fieldOf("roots").forGetter(ServerSelectQiRootPacket::roots)
    ).apply(instance, ServerSelectQiRootPacket::new)).codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerSelectQiRootPacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    @Override
    public void process(ServerPacketContext serverPacketContext) {
        PlayerUtil.setRoots(serverPacketContext.player(), roots());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
