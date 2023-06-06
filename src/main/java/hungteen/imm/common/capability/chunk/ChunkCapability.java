package hungteen.imm.common.capability.chunk;

import hungteen.imm.common.world.LevelManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/11 16:36
 */
public class ChunkCapability implements IChunkCapability{

    private LevelChunk chunk;
    private float spiritualRate;

    public void init(LevelChunk chunk){
        this.chunk = chunk;
        this.spiritualRate = LevelManager.getChunkSpiritualRate(chunk);
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putFloat("SpiritualRate", this.spiritualRate);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        if(tag.contains("SpiritualRate")){
            this.spiritualRate = tag.getFloat("SpiritualRate");
        }
    }

    public float getSpiritualRate() {
        return spiritualRate;
    }
}
