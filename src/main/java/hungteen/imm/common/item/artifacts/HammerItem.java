package hungteen.imm.common.item.artifacts;

import hungteen.imm.api.interfaces.IArtifactTier;
import hungteen.imm.common.blockentity.SmithingArtifactBlockEntity;
import hungteen.imm.common.item.ImmortalToolActions;
import hungteen.imm.utils.PlayerUtil;
import hungteen.imm.utils.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class HammerItem extends MeleeAttackItem {

    public static final int SMITHING_COOL_DOWN = 60;
    private static final int LEAST_USING_TICK = 20;
    private static final String VALID_POS = "ValidPos";
    private static final String BLOCK_POS = "BlockPos";

    public HammerItem(IArtifactTier tier) {
        super(MeleeAttackTypes.HAMMER, tier);
    }

    /**
     * {@link hungteen.imm.common.event.ImmortalPlayerEvents#onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock)}
     */
    public static void smithing(LivingEntity livingEntity, InteractionHand hand, Direction direction, BlockPos blockPos){
        if(canPerformSmithing(livingEntity.getItemInHand(hand)) && direction == Direction.UP){
            if(livingEntity instanceof Player && PlayerUtil.isInCD((Player) livingEntity, livingEntity.getItemInHand(hand).getItem())){
                return;
            }
//            if(livingEntity.level.getBlockEntity(blockPos) instanceof SmithingArtifactBlockEntity){
//                if(! livingEntity.level.isClientSide){
//
//                }
//                livingEntity.swing(hand);
//                if(livingEntity instanceof Player){
//                    PlayerUtil.setCoolDown((Player) livingEntity, livingEntity.getItemInHand(hand).getItem(), SMITHING_COOL_DOWN);
//                    Util.getProxy().onSmithing(blockPos, hand == InteractionHand.MAIN_HAND);
//                }
//            }
        }
    }

    public static boolean canPerformSmithing(ItemStack stack){
        return stack.canPerformAction(ImmortalToolActions.ARTIFACT_SMITHING);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {

    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int count) {

    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 1200;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return super.canPerformAction(stack, toolAction) || ImmortalToolActions.DEFAULT_HAMMER_ACTIONS.contains(toolAction);
    }
}
