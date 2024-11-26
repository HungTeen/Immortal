package hungteen.imm.common.network.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.network.ClientPacketContext;
import hungteen.htlib.common.network.packet.PlayToClientPacket;
import hungteen.imm.common.capability.player.IMMPlayerData;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-13 21:34
 **/
public record FloatDataPacket(IMMPlayerData.FloatData data, float value) implements PlayToClientPacket {

    public static final CustomPacketPayload.Type<FloatDataPacket> TYPE = new CustomPacketPayload.Type<>(Util.prefix("float_data"));

    public static final Codec<FloatDataPacket> CODEC = RecordCodecBuilder.<FloatDataPacket>mapCodec(instance -> instance.group(
            IMMPlayerData.FloatData.CODEC.fieldOf("type").forGetter(FloatDataPacket::data),
            Codec.FLOAT.fieldOf("value").forGetter(FloatDataPacket::value)
    ).apply(instance, FloatDataPacket::new)).codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, FloatDataPacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    @Override
    public void process(ClientPacketContext clientPacketContext) {
        PlayerUtil.setFloatData(clientPacketContext.player(), data, value);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
