package hungteen.imm.common.blockentity;

import hungteen.htlib.common.blockentity.ItemHandlerBlockEntity;
import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.registry.IArtifactType;
import hungteen.imm.common.block.artifacts.SpiritualFurnaceBlock;
import hungteen.imm.common.impl.ArtifactTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;

import javax.annotation.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-07-12 19:47
 **/
public abstract class FunctionalFurnaceBlockEntity extends ItemHandlerBlockEntity implements MenuProvider {

    protected boolean start = false;
    private BlockPattern.BlockPatternMatch lastMatch;
    private SpiritualFurnaceBlockEntity furnaceBlockEntity;

    public FunctionalFurnaceBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState state) {
        super(type, blockPos, state);
    }

    /**
     * Lazy update furnace.
     */
    public void opened(){
        this.getFurnaceBlockEntity();
    }

    public void serverTick() {
        if(this.started()){
            // Furnace is broken !
            if(! SpiritualFurnaceBlock.persist(this.level, this.getLastMatch())){
                this.explode();
            }
        }
    }

    /**
     * Click white flame to start.
     */
    public void onStart(Level level) {
        this.start = true;
        this.update();
    }

    public void onFinish(){
        this.start = false;
        this.update();
    }

    public void explode(){
//        this.level.explode(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 10, true, Explosion.BlockInteraction.DESTROY);
//        getBelowBlockEntity().ifPresent(SpiritualFurnaceBlockEntity::stop);
//        this.reset();
    }

    /**
     * Must have furnace below.
     */
    public boolean canStart(){
        return ! started() && getFurnaceBlockEntity() != null && getFurnaceBlockEntity().canSmelt();
    }

    public boolean started(){
        return this.start;
    }

    public void update(){
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        this.setChanged();
    }

    public BlockPattern.BlockPatternMatch getLastMatch(){
        if(this.lastMatch == null){
            this.lastMatch = SpiritualFurnaceBlock.getMatch(this.level, this.getBlockPos());
        }
        return this.lastMatch;
    }

    @Nullable
    public SpiritualFurnaceBlockEntity getFurnaceBlockEntity(){
        if(this.furnaceBlockEntity == null){
            if(this.getLastMatch() != null){
                this.furnaceBlockEntity = SpiritualFurnaceBlock.getFurnaceBlockEntity(this.getLastMatch());
                if(this.furnaceBlockEntity == null){
                    this.lastMatch = null;
                }
            }
        }
        return this.furnaceBlockEntity;
    }

    public IArtifactType getArtifactType() {
        return this.getBlockState().getBlock() instanceof IArtifactBlock ? ((IArtifactBlock) this.getBlockState().getBlock()).getArtifactType(this.getBlockState()) : ArtifactTypes.EMPTY;
    }

}
