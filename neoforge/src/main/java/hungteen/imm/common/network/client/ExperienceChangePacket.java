package hungteen.imm.common.network.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.network.ClientPacketContext;
import hungteen.htlib.common.network.packet.PlayToClientPacket;
import hungteen.imm.api.cultivation.ExperienceType;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ExtraCodecs;

import java.util.Map;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 16:43
 **/
public record ExperienceChangePacket(Map<ExperienceType, Float> xpMap) implements PlayToClientPacket {

    public static final Type<ExperienceChangePacket> TYPE = new Type<>(Util.prefix("root_and_realm"));
    public static final Codec<ExperienceChangePacket> CODEC = RecordCodecBuilder.<ExperienceChangePacket>mapCodec(instance -> instance.group(
            ExtraCodecs.strictUnboundedMap(ExperienceType.CODEC, Codec.floatRange(0, Float.MAX_VALUE)).fieldOf("xpMap").forGetter(ExperienceChangePacket::xpMap)
    ).apply(instance, ExperienceChangePacket::new)).codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, ExperienceChangePacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    @Override
    public void process(ClientPacketContext clientPacketContext) {
        PlayerUtil.setData(clientPacketContext.player(), data -> {
            xpMap().forEach((type, value) -> {
                data.getCultivationData().setExperience(type, value);
            });
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
