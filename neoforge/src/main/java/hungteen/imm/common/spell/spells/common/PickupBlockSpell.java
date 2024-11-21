package hungteen.imm.common.spell.spells.common;

import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.spell.spells.SpellType;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/6 19:30
 */
public class PickupBlockSpell extends SpellType {

    public PickupBlockSpell() {
        super("pickup_block", properties().maxLevel(2).mana(10).cd(100));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if (result.hasBlock() && owner instanceof Player player) {
            if(! EntityUtil.hasEmptyHand(owner)){
                this.sendTip(owner, "no_empty_hand");
                return false;
            }
            final BlockState state = result.getBlockState(player.level());
            // ban bedrock like blocks.
            if (state != null && state.getDestroySpeed(player.level(), Objects.requireNonNull(result.getBlockPos())) >= 0) {
                final BlockEntity blockentity = state.hasBlockEntity() ? player.level().getBlockEntity(result.getBlockPos()) : null;
                final ItemStack stack = new ItemStack(state.getBlock().asItem());
//                if (blockentity != null) {
//                    blockentity.saveToItem(stack);
//                    if (blockentity instanceof Nameable && ((Nameable) blockentity).hasCustomName()) {
//                        stack.setHoverName(((Nameable) blockentity).getCustomName());
//                    }
//                }
                if(blockentity == null && ! stack.isEmpty()){
                    if(! state.liquid() && (! state.isSolid() || level > 1)){
                        PlayerUtil.addItem(player, stack);
                        player.level().setBlock(result.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ClipContext.Block getBlockClipMode(int level) {
        return ClipContext.Block.OUTLINE;
    }
}