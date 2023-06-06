package hungteen.imm.util;

import hungteen.imm.common.capability.CapabilityHandler;
import hungteen.imm.common.capability.chunk.ChunkCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-05 21:59
 **/
public class LevelUtil {

    public static Optional<ChunkCapability> getChunkCapOpt(Level level, BlockPos pos){
        return Optional.ofNullable(getChunkCapability(level, pos));
    }

    @Nullable
    public static ChunkCapability getChunkCapability(Level level, BlockPos pos){
        return level.getChunkAt(pos).getCapability(CapabilityHandler.CHUNK_CAP).resolve().orElse(null);
    }

}
