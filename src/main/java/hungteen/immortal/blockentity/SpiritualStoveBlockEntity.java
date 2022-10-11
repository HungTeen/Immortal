package hungteen.immortal.blockentity;

import hungteen.htlib.blockentity.HTNameableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 09:30
 **/
public class SpiritualStoveBlockEntity extends HTNameableBlockEntity {

    public SpiritualStoveBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ImmortalBlockEntities.SPIRITUAL_STOVE.get(), blockPos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("gui.immortal.spiritual_stove");
    }
}
