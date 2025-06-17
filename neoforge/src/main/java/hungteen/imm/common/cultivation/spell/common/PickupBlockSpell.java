package hungteen.imm.common.cultivation.spell.common;

import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/6 19:30
 */
public class PickupBlockSpell extends SpellTypeImpl {

    public PickupBlockSpell() {
        super("pickup_block", property().maxLevel(2).mana(10).cd(100));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        if(context.targetStateOpt().isPresent() && context.owner() instanceof Player player) {
            Optional<InteractionHand> handOpt = EntityUtil.getEmptyHand(context.owner());
            if (handOpt.isEmpty()) {
                sendTip(context.owner(), NO_EMPTY_HAND);
                return false;
            }
            final BlockState state = context.targetState();
            // ban bedrock like blocks.
            if (state.getDestroySpeed(player.level(), context.targetPos()) >= 0) {
                final BlockEntity blockEntity = state.hasBlockEntity() ? player.level().getBlockEntity(context.targetPos()) : null;
                final ItemStack stack = new ItemStack(state.getBlock().asItem());
                if (blockEntity == null && !stack.isEmpty()) {
                    if (!state.liquid() && (!state.isSolid() || context.spellLevel() > 1)) {
                        PlayerUtil.addItem(player, stack);
                        player.swing(handOpt.get());
                        context.level().setBlock(context.targetPos(), Blocks.AIR.defaultBlockState(), 3);
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