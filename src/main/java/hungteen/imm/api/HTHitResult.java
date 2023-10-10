package hungteen.imm.api;

import hungteen.imm.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/6 19:42
 */
public class HTHitResult {

    private static final HTHitResult MISS = new HTHitResult(null);
    private final Entity entity;
    private final BlockPos pos;
    private final Direction direction;

    public HTHitResult(Entity entity) {
        this(entity, null, null);
    }

    public HTHitResult(BlockPos pos, Direction direction) {
        this(null, pos, direction);
    }

    public HTHitResult(Entity entity, BlockPos pos, Direction direction) {
        this.entity = entity;
        this.pos = pos;
        this.direction = direction;
    }

    public static @NotNull HTHitResult create(Entity entity, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
        return create(EntityUtil.getHitResult(entity, blockMode, fluidMode));
    }

    public static @NotNull HTHitResult create(HitResult hitResult) {
        if(hitResult instanceof EntityHitResult result){
            return new HTHitResult(result.getEntity());
        } else if(hitResult instanceof BlockHitResult result){
            return new HTHitResult(result.getBlockPos(), result.getDirection());
        }
        return HTHitResult.miss();
    }

    public static HTHitResult miss() {
        return MISS;
    }

    public boolean isMiss(){
        return this == MISS;
    }

    public boolean hasEntity(){
        return this.getEntity() != null;
    }

    public boolean hasBlock(){
        return this.getBlockPos() != null;
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
    public BlockState getBlockState(Level level){
        return this.getBlockPos() == null ? null : level.getBlockState(this.getBlockPos());
    }

}
