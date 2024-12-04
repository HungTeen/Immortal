package hungteen.imm.api.artifact;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

/**
 * 法器的品质。
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/4 14:45
 **/
public enum ArtifactRank implements StringRepresentable {

    /**
     * 未知品质。
     */
    UNKNOWN,

    /**
     * 低阶法器。
     */
    COMMON,

    /**
     * 中阶法器。
     */
    MODERATE,

    /**
     * 高阶法器。
     */
    ADVANCED,

    /**
     * 顶阶法器。
     */
    SUPREME,

    ;

    public static final Codec<ArtifactRank> CODEC = StringRepresentable.fromEnum(ArtifactRank::values);
    public static final IntFunction<ArtifactRank> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, ArtifactRank> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
