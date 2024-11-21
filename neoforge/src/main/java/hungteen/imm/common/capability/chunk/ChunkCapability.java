package hungteen.imm.common.capability.chunk;

import hungteen.imm.common.world.LevelManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/11 16:36
 */
public class ChunkCapability implements IChunkCapability{

    private LevelChunk chunk;
    private Optional<Float> spiritualRate = Optional.empty();

    public void init(LevelChunk chunk){
        this.chunk = chunk;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        this.spiritualRate.ifPresent(aFloat -> tag.putFloat("SpiritualRate", aFloat));
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        if(tag.contains("SpiritualRate")){
            this.spiritualRate = Optional.of(tag.getFloat("SpiritualRate"));
        }
    }

    public float getSpiritualRate() {
        if(this.spiritualRate.isEmpty()){
            this.spiritualRate = LevelManager.getChunkSpiritualRate(chunk);
        }
        return this.spiritualRate.orElse(0F);
    }
}
