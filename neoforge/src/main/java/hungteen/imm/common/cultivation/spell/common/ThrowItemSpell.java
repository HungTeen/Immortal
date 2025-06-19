package hungteen.imm.common.cultivation.spell.common;

import hungteen.htlib.util.helper.JavaHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.common.entity.misc.ThrowingItemEntity;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.LevelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/17 16:48
 */
public class ThrowItemSpell extends SpellTypeImpl {

    public ThrowItemSpell() {
        super("throw_item", property(SpellUsageCategory.ATTACK_TARGET, InscriptionTypes.ANY).maxLevel(2).qi(25).cd(80));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        ItemStack throwItem = ItemStack.EMPTY;
        if(context.itemTrigger()){
            throwItem = context.usingItem();
        } else if(EntityUtil.hasItemInHand(context.owner())){
            throwItem = EntityUtil.getItemInHand(context.owner(), JavaHelper::alwaysTrue);
        }
        if(! throwItem.isEmpty()) {
            final ThrowingItemEntity projectile = new ThrowingItemEntity(context.owner(), context.level());
            projectile.setSpellLevel(context.spellLevel());
            projectile.setItem(throwItem.copy());
            if(context.owner() instanceof Player || context.targetStateOpt().isEmpty()){
                projectile.shootFromRotation(context.owner(), context.owner().getXRot(), context.owner().getYRot(), 0.0F, 3F, 1.0F);
            } else {
                EntityUtil.shootProjectile(projectile, context.target().position().subtract(context.owner().getEyePosition()), 1.2F, 2F);
            }
            if(context.level().addFreshEntity(projectile)){
                throwItem.shrink(1);
                LevelUtil.playSound(context.level(), SoundEvents.TRIDENT_THROW.value(), SoundSource.NEUTRAL, projectile.position(), 1F, 1F);
                return true;
            } else {
                IMMAPI.logger().error("Failed to throw item entity");
            }
        }
        sendTip(context, NO_ITEM_IN_HANDS);
        return false;
    }

    /**
     * 玩家使用物品破坏方块。{@link net.minecraft.server.level.ServerPlayerGameMode#destroyBlock(BlockPos)}
     * @param usingItem 使用的物品。
     */
    public static boolean destroyBlock(ServerPlayer player, Level level, BlockPos pos, ItemStack usingItem) {
        BlockState blockstate1 = level.getBlockState(pos);
        BlockEvent.BreakEvent event = CommonHooks.fireBlockBreak(level, player.gameMode.getGameModeForPlayer(), player, pos, blockstate1);
        if (event.isCanceled()) {
            return false;
        } else {
            BlockEntity blockentity = level.getBlockEntity(pos);
            Block block = blockstate1.getBlock();
            if (block instanceof GameMasterBlock && !player.canUseGameMasterBlocks()) {
                level.sendBlockUpdated(pos, blockstate1, blockstate1, 3);
                return false;
            } else if (player.blockActionRestricted(level, pos, player.gameMode.getGameModeForPlayer())) {
                return false;
            } else {
                BlockState blockstate = block.playerWillDestroy(level, pos, blockstate1, player);
                if (player.isCreative()) {
                    player.gameMode.removeBlock(pos, blockstate, false);
                    return true;
                } else {
                    ItemStack itemstack = usingItem;
                    ItemStack itemstack1 = itemstack.copy();
                    boolean flag1 = itemstack.isCorrectToolForDrops(blockstate);
                    itemstack.mineBlock(level, blockstate, pos, player);
                    boolean flag = player.gameMode.removeBlock(pos, blockstate, flag1);
                    if (flag1 && flag) {
                        block.playerDestroy(level, player, pos, blockstate, blockentity, itemstack1);
                    }

                    if (itemstack.isEmpty() && !itemstack1.isEmpty()) {
                        EventHooks.onPlayerDestroyItem(player, itemstack1, InteractionHand.MAIN_HAND);
                    }

                    return true;
                }
            }
        }
    }
    
}
