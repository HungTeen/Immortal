package hungteen.imm.common.network.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.network.ServerPacketContext;
import hungteen.htlib.common.network.packet.PlayToServerPacket;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/18 15:08
 */
public record ScreenOperationPacket(OperationType operationType, int value) implements PlayToServerPacket {

    public static final CustomPacketPayload.Type<ScreenOperationPacket> TYPE = new CustomPacketPayload.Type<>(Util.prefix("screen_button"));
    public static final Codec<ScreenOperationPacket> CODEC = RecordCodecBuilder.<ScreenOperationPacket>mapCodec(instance -> instance.group(
            OperationType.CODEC.fieldOf("operation_type").forGetter(ScreenOperationPacket::operationType),
            Codec.INT.fieldOf("value").forGetter(ScreenOperationPacket::value)
    ).apply(instance, ScreenOperationPacket::new)).codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, ScreenOperationPacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public ScreenOperationPacket(OperationType operationType) {
        this(operationType, 0);
    }

    @Override
    public void process(ServerPacketContext serverPacketContext) {
        ServerPlayer player = serverPacketContext.player();
        switch (operationType()) {
            case MEDITATE -> {
                CultivationManager.meditate(player);
            }
            case QUIT_MEDITATION -> {
                CultivationManager.quitMeditate(player);
            }
            case BREAK_THROUGH -> {
                CultivationManager.breakThroughStart(player);
            }
            case SET_SPAWN_POINT -> {
                // See SetSpawnCommand.
                player.setRespawnPosition(player.level().dimension(), player.blockPosition().above(), 0, true, false);
            }
            case QUIT_RESTING -> PlayerUtil.quitResting(player);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum OperationType implements StringRepresentable {

        MEDITATE,

        QUIT_MEDITATION,

        BREAK_THROUGH,

        SET_SPAWN_POINT,

        QUIT_RESTING,

        ;

        public static final Codec<OperationType> CODEC = StringRepresentable.fromEnum(OperationType::values);

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

}
