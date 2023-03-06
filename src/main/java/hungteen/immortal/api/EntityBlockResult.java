package hungteen.immortal.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/6 19:42
 */
public class EntityBlockResult {

    private final Level level;
    private Entity entity;
    private BlockPos pos;
    private Direction direction;

    public EntityBlockResult(Level level, Entity entity) {
        this(level, entity, null, null);
    }

    public EntityBlockResult(Level level, BlockPos pos, Direction direction) {
        this(level, null, pos, direction);
    }

    public EntityBlockResult(Level level, Entity entity, BlockPos pos, Direction direction) {
        this.level = level;
        this.entity = entity;
        this.pos = pos;
        this.direction = direction;
    }

    public boolean hasEntity(){
        return this.getEntity() != null;
    }

    public boolean hasBlock(){
        return this.getBlockPos() != null;
    }

    public Level getLevel() {
        return level;
    }

    @Nullable
    public Entity getEntity(){
        return this.entity;
    }

    @Nullable
    public BlockPos getBlockPos(){
        return this.pos;
    }

    @Nullable
    public Direction getDirection(){
        return this.direction;
    }

    @Nullable
    public BlockState getBlockState(){
        return this.getBlockPos() == null ? null : this.level.getBlockState(this.getBlockPos());
    }

}
