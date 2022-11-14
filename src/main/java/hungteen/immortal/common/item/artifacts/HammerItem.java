package hungteen.immortal.common.item.artifacts;

import hungteen.immortal.api.registry.IArtifactTier;
import hungteen.immortal.common.blockentity.SmithingArtifactBlockEntity;
import hungteen.immortal.common.item.ImmortalToolActions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ToolAction;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class HammerItem extends MeleeAttackItem {

    private static final int LEAST_USING_TICK = 20;
    private static final String VALID_POS = "ValidPos";
    private static final String BLOCK_POS = "BlockPos";

    public HammerItem(boolean isAncientArtifact, IArtifactTier tier) {
        super(MeleeAttackTypes.HAMMER, isAncientArtifact, tier);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        final BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if(blockEntity instanceof SmithingArtifactBlockEntity){
            ((SmithingArtifactBlockEntity) blockEntity).onSmithing();
            setBlockPos(context.getItemInHand(), context.getClickedPos());
            context.getPlayer().startUsingItem(context.getHand());
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if(validUse(stack, count)){
            setValidPos(stack, true);
        } else{
            setValidPos(stack, false);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int count) {
        if(validUse(stack, count)){
            BlockPos pos = getBlockPos(stack);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof SmithingArtifactBlockEntity){
                ((SmithingArtifactBlockEntity) blockEntity).finishSmithing();
            }
            setValidPos(stack, false);
        }
    }

    public boolean validUse(ItemStack stack, int count) {
        return count + LEAST_USING_TICK < getUseDuration(stack);
    }

    public static void setValidPos(ItemStack stack, boolean valid){
        stack.getOrCreateTag().putBoolean(VALID_POS, valid);
    }

    public static boolean isValidPos(ItemStack stack){
        return stack.getOrCreateTag().getBoolean(VALID_POS);
    }

    public static void setBlockPos(ItemStack stack, BlockPos pos){
        stack.getOrCreateTag().putLong(BLOCK_POS, pos.asLong());
    }

    public static BlockPos getBlockPos(ItemStack stack){
        return BlockPos.of(stack.getOrCreateTag().getLong(BLOCK_POS));
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 1200;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return super.canPerformAction(stack, toolAction) || ImmortalToolActions.DEFAULT_HAMMER_ACTIONS.contains(toolAction);
    }
}
