package hungteen.imm.common.network.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.network.ClientPacketContext;
import hungteen.htlib.common.network.packet.PlayToClientPacket;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-26 18:48
 **/
public record MiscDataPacket(MiscType miscType, String data, float value) implements PlayToClientPacket {

    public static final Type<MiscDataPacket> TYPE = new Type<>(Util.prefix("misc_data"));

    public static final Codec<MiscDataPacket> CODEC = RecordCodecBuilder.<MiscDataPacket>mapCodec(instance -> instance.group(
            MiscType.CODEC.fieldOf("type").forGetter(MiscDataPacket::miscType),
            Codec.STRING.fieldOf("data").forGetter(MiscDataPacket::data),
            Codec.FLOAT.fieldOf("value").forGetter(MiscDataPacket::value)
    ).apply(instance, MiscDataPacket::new)).codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, MiscDataPacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    @Override
    public void process(ClientPacketContext clientPacketContext) {
        switch (miscType()){
            case OPEN_QI_ROOT_SCREEN -> {
                if(PlayerUtil.getRoots(clientPacketContext.player()).isEmpty()){
                    Util.getProxy().openQiRootScreen(clientPacketContext.player());
                }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum MiscType implements StringRepresentable {

        OPEN_QI_ROOT_SCREEN,

        ;

        public static final Codec<MiscType> CODEC = StringRepresentable.fromEnum(MiscType::values);

        @Override
        public String getSerializedName() {
            return Util.capitalize(name().toLowerCase());
        }
    }

}
