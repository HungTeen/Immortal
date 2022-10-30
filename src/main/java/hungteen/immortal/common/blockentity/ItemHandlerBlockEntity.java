package hungteen.immortal.common.blockentity;

import hungteen.htlib.blockentity.HTNameableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-30 14:16
 **/
public abstract class ItemHandlerBlockEntity  extends HTNameableBlockEntity {

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public ItemHandlerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState state) {
        super(blockEntityType, blockPos, state);
    }

    public abstract ItemStackHandler getItemHandler();

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.lazyItemHandler = LazyOptional.of(this::getItemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("ItemHandler", this.getItemHandler().serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("ItemHandler")){
            this.getItemHandler().deserializeNBT(tag.getCompound("ItemHandler"));
        }
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("GrassCarp");
    }
}
