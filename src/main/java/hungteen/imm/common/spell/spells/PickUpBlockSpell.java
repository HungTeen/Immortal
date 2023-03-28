package hungteen.imm.common.spell.spells;

import hungteen.imm.api.EntityBlockResult;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.utils.PlayerUtil;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/6 19:30
 */
public class PickUpBlockSpell extends SpellTypes.SpellType {

    public PickUpBlockSpell(SpellTypes.SpellProperties properties) {
        super("block_picking", properties);
    }

    @Override
    public boolean onActivate(LivingEntity owner, EntityBlockResult result, int level) {
        if (result.hasBlock() && owner instanceof Player player) {
            final BlockState state = player.level.getBlockState(result.getBlockPos());
            // ban bedrock like blocks.
            if (state.getDestroySpeed(player.level, result.getBlockPos()) >= 0) {
                final BlockEntity blockentity = state.hasBlockEntity() ? player.level.getBlockEntity(result.getBlockPos()) : null;
                final ItemStack stack = new ItemStack(state.getBlock().asItem());
                if (blockentity != null) {
                    blockentity.saveToItem(stack);
                    if (blockentity instanceof Nameable && ((Nameable) blockentity).hasCustomName()) {
                        stack.setHoverName(((Nameable) blockentity).getCustomName());
                    }
                }
                PlayerUtil.addItem(player, stack);
                player.level.setBlock(result.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
            }
        }
        return false;
    }

    @Override
    public boolean isPassiveSpell() {
        return false;
    }
}