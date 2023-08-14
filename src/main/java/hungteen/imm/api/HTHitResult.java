package hungteen.imm.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/6 19:42
 */
public class HTHitResult {

    private static final HTHitResult MISS = new HTHitResult(null);
    private Entity entity;
    private BlockPos pos;
    private Direction direction;

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

    public static HTHitResult create(HitResult hitResult) {
        if(hitResult instanceof EntityHitResult result){
            return new HTHitResult(result.getEntity());
        } else if(hitResult instanceof BlockHitResult result){
            return new HTHitResult(result.getBlockPos(), result.getDirection());
        }
        return null;
    }

    public static HTHitResult miss() {
        return MISS;
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
