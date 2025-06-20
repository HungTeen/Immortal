package hungteen.imm.common.item.artifact;

import hungteen.imm.api.artifact.ArtifactTier;
import hungteen.imm.common.event.IMMPlayerEvents;
import hungteen.imm.common.item.IMMToolActions;
import hungteen.imm.util.PlayerUtil;
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
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-19 22:31
 **/
public class HammerItem extends MeleeAttackItem {

    public static final int SMITHING_COOL_DOWN = 60;
    private static final int LEAST_USING_TICK = 20;
    private static final String VALID_POS = "ValidPos";
    private static final String BLOCK_POS = "BlockPos";

    public HammerItem(ArtifactTier tier) {
        super(MeleeAttackTypes.HAMMER, tier);
    }

    /**
     * {@link IMMPlayerEvents#onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock)}
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
        return stack.canPerformAction(IMMToolActions.ARTIFACT_SMITHING);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity player, ItemStack stack, int count) {

    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int count) {

    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity living) {
        return 1200;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility ability) {
        return super.canPerformAction(stack, ability) || IMMToolActions.DEFAULT_HAMMER_ACTIONS.contains(ability);
    }
}
