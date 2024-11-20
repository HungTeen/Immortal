package hungteen.imm.mixin;

import hungteen.imm.common.block.IMMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ScaffoldingBlock.class)
public class MixinScaffoldingBlock {

    @Inject(method = "getDistance(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)I",
            at = @At(
                    value = "RETURN"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private static void getDistance(BlockGetter getter, BlockPos pos, CallbackInfoReturnable<Integer> cir, BlockPos.MutableBlockPos belowPos, BlockState blockstate, int i) {
        if (blockstate.is(IMMBlocks.GOURD_SCAFFOLD.get())) {
            i = Math.min(i, blockstate.getValue(ScaffoldingBlock.DISTANCE));
        } else if (blockstate.isFaceSturdy(getter, belowPos, Direction.UP)) {
            cir.setReturnValue(0);
            return;
        }
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState blockstate1 = getter.getBlockState(belowPos.setWithOffset(pos, direction));
            if (blockstate1.is(IMMBlocks.GOURD_SCAFFOLD.get())) {
                i = Math.min(i, blockstate1.getValue(ScaffoldingBlock.DISTANCE) + 1);
                if (i == 1) {
                    break;
                }
            }
        }
        cir.setReturnValue(i);
    }

    @Inject(method = "isBottom(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;I)Z",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void isBottom(BlockGetter p_56028_, BlockPos p_56029_, int p_56030_, CallbackInfoReturnable<Boolean> cir) {
        if(p_56028_.getBlockState(p_56029_.below()).is(IMMBlocks.GOURD_SCAFFOLD.get())){
            cir.setReturnValue(false);
        }
    }

}
