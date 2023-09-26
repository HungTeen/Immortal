package hungteen.imm.common.blockentity;

import hungteen.htlib.common.blockentity.ItemHandlerBlockEntity;
import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.registry.IArtifactType;
import hungteen.imm.common.block.artifacts.SpiritualFurnaceBlock;
import hungteen.imm.common.impl.ArtifactTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;

import javax.annotation.Nullable;
import java.util.Optional;

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

    public boolean canContinue(){
        return SpiritualFurnaceBlock.persist(this.level, this.getLastMatch());
    }

    /**
     * Click white flame to start.
     */
    public void onStart(Level level){
        this.getFurnaceOpt().ifPresent(furnace -> {
            this.start = true;
            furnace.start();
        });
    }

    public void onFinish(){
        this.start = false;
    }

    public boolean requireFlame(){
        return this.start;
    }

    /**
     * Must have furnace below.
     */
    public boolean canStart(){
        return ! started() && getFurnaceBlockEntity() != null && getFurnaceBlockEntity().enoughFlame();
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
            if(this.level != null){
                final BlockEntity blockEntity = this.level.getBlockEntity(this.getBlockPos().below());
                if(blockEntity instanceof MiniFurnaceBlockEntity entity){
                    this.furnaceBlockEntity = entity;
                }
            }
            if(this.furnaceBlockEntity == null && this.getLastMatch() != null){
                this.furnaceBlockEntity = SpiritualFurnaceBlock.getFurnaceBlockEntity(this.getLastMatch());
                if(this.furnaceBlockEntity == null){
                    this.lastMatch = null;
                }
            }
        }
        return this.furnaceBlockEntity;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("SmeltingStart")){
            this.start = tag.getBoolean("SmeltingStart");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("SmeltingStart", this.start);
    }

    public Optional<SpiritualFurnaceBlockEntity> getFurnaceOpt(){
        return Optional.ofNullable(this.getFurnaceBlockEntity());
    }

    public boolean isMiniFurnace(){
        return this.furnaceBlockEntity instanceof MiniFurnaceBlockEntity;
    }

    public boolean isLargeFurnace(){
        return this.furnaceBlockEntity != null && ! isMiniFurnace();
    }

    public IArtifactType getArtifactType() {
        return this.getBlockState().getBlock() instanceof IArtifactBlock ? ((IArtifactBlock) this.getBlockState().getBlock()).getArtifactType(this.getBlockState()) : ArtifactTypes.UNKNOWN;
    }

}
