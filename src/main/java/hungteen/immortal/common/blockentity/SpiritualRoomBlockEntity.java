package hungteen.immortal.common.blockentity;

import hungteen.htlib.blockentity.ItemHandlerBlockEntity;
import hungteen.immortal.api.interfaces.IArtifact;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-13 21:59
 **/
public class SpiritualRoomBlockEntity extends ItemHandlerBlockEntity implements IArtifact {

    protected final ItemStackHandler itemHandler = new ItemStackHandler(1);

    public SpiritualRoomBlockEntity(BlockPos blockPos, BlockState state) {
        super(ImmortalBlockEntities.SPIRITUAL_ROOM.get(), blockPos, state);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState state, SpiritualRoomBlockEntity blockEntity) {

    }

    @Override
    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public int getArtifactLevel() {
        return this.getBlockState().getBlock() instanceof IArtifact ? ((IArtifact) this.getBlockState().getBlock()).getArtifactLevel() : 0;
    }

}
