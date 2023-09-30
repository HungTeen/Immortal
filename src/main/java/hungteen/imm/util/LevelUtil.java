package hungteen.imm.util;

import hungteen.imm.common.capability.CapabilityHandler;
import hungteen.imm.common.capability.chunk.ChunkCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-05 21:59
 **/
public class LevelUtil {

    public static void playSound(Level level, SoundEvent sound, SoundSource source, Vec3 pos) {
        playSound(level, sound, source, pos, 1F, 1F);
    }

    public static void playSound(Level level, SoundEvent sound, SoundSource source, Vec3 pos, float volume, float pitch) {
        level.playSound(null, pos.x(), pos.y(), pos.z(), sound, source, volume, pitch / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public static float getSpiritualRate(Level level, BlockPos pos) {
        return LevelUtil.getChunkCapOpt(level, pos).map(ChunkCapability::getSpiritualRate).orElse(0F);
    }

    public static Optional<ChunkCapability> getChunkCapOpt(Level level, BlockPos pos) {
        return Optional.ofNullable(getChunkCapability(level, pos));
    }

    @Nullable
    public static ChunkCapability getChunkCapability(Level level, BlockPos pos) {
        return level.getChunkAt(pos).getCapability(CapabilityHandler.CHUNK_CAP).resolve().orElse(null);
    }

}
