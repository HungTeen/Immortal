package hungteen.imm.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import org.jetbrains.annotations.Nullable;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-09 09:30
 **/
public class MiniFurnaceBlockEntity extends SpiritualFurnaceBlockEntity {

    public MiniFurnaceBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public BlockPattern.BlockPatternMatch getLastMatch() {
        return null;
    }

    @Nullable
    @Override
    public FunctionalFurnaceBlockEntity getFunctionalBlockEntity() {
        if(this.functionalBlockEntity == null && this.level != null){
            final BlockEntity blockEntity = this.level.getBlockEntity(this.getBlockPos().above());
            if(blockEntity instanceof FunctionalFurnaceBlockEntity entity){
                this.functionalBlockEntity = entity;
            }
        }
        return this.functionalBlockEntity;
    }
}
