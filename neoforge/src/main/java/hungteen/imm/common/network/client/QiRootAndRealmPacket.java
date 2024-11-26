package hungteen.imm.common.network.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.network.ClientPacketContext;
import hungteen.htlib.common.network.packet.PlayToClientPacket;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.capability.player.CultivationData;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 16:43
 **/
public record QiRootAndRealmPacket(List<QiRootType> roots, RealmType realmType) implements PlayToClientPacket {

    public static final CustomPacketPayload.Type<QiRootAndRealmPacket> TYPE = new CustomPacketPayload.Type<>(Util.prefix("root_and_realm"));
    public static final Codec<QiRootAndRealmPacket> CODEC = RecordCodecBuilder.<QiRootAndRealmPacket>mapCodec(instance -> instance.group(
            QiRootTypes.registry().byNameCodec().listOf().fieldOf("roots").forGetter(QiRootAndRealmPacket::roots),
            RealmTypes.registry().byNameCodec().fieldOf("realmType").forGetter(QiRootAndRealmPacket::realmType)
    ).apply(instance, QiRootAndRealmPacket::new)).codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, QiRootAndRealmPacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    @Override
    public void process(ClientPacketContext clientPacketContext) {
        PlayerUtil.setData(clientPacketContext.player(), data -> {
            CultivationData cultivationData = data.getCultivationData();
            cultivationData.clearSpiritualRoot();
            roots.forEach(cultivationData::addRoot);
            cultivationData.setRealmType(realmType);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
