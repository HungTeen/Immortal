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
public record IntegerDataPacket(IMMPlayerData.IntegerData data, int value) implements PlayToClientPacket {

    public static final Type<IntegerDataPacket> TYPE = new Type<>(Util.prefix("integer_data"));

    public static final Codec<IntegerDataPacket> CODEC = RecordCodecBuilder.<IntegerDataPacket>mapCodec(instance -> instance.group(
            IMMPlayerData.IntegerData.CODEC.fieldOf("type").forGetter(IntegerDataPacket::data),
            Codec.INT.fieldOf("value").forGetter(IntegerDataPacket::value)
    ).apply(instance, IntegerDataPacket::new)).codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, IntegerDataPacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    @Override
    public void process(ClientPacketContext clientPacketContext) {
        PlayerUtil.setIntegerData(clientPacketContext.player(), data, value);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
