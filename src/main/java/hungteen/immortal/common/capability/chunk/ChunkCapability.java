package hungteen.immortal.common.capability.chunk;

import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.common.impl.registry.SpiritualTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashMap;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/11 16:36
 */
public class ChunkCapability implements IChunkCapability{

    private LevelChunk chunk;

    public void init(LevelChunk chunk){
        this.chunk = chunk;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
    }

}
