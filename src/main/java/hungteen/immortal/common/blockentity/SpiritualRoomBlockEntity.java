package hungteen.immortal.common.blockentity;

import hungteen.htlib.common.blockentity.ItemHandlerBlockEntity;
import hungteen.immortal.api.interfaces.IArtifactBlock;
import hungteen.immortal.api.interfaces.IArtifactItem;
import hungteen.immortal.api.registry.IArtifactType;
import hungteen.immortal.common.impl.registry.ArtifactTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-13 21:59
 **/
public class SpiritualRoomBlockEntity extends ItemHandlerBlockEntity {

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

    public IArtifactType getArtifactType() {
        return this.getBlockState().getBlock() instanceof IArtifactBlock ? ((IArtifactBlock) this.getBlockState().getBlock()).getArtifactType(this.getBlockState()) : ArtifactTypes.EMPTY;
    }

}
