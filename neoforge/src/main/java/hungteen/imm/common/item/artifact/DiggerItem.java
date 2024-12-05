package hungteen.imm.common.item.artifact;

import hungteen.imm.api.artifact.IArtifactTier;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-11 11:14
 **/
public abstract class DiggerItem extends MeleeAttackItem{

    private final TagKey<Block> fitBlockTag;
    protected final float digSpeed;

    public DiggerItem(MeleeAttackTypes meleeAttackType, IArtifactTier tier, float digSpeed, TagKey<Block> fitBlockTag) {
        super(meleeAttackType, tier);
        this.digSpeed = digSpeed;
        this.fitBlockTag = fitBlockTag;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos blockPos, Player player) {
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(fitBlockTag) ? this.digSpeed : 1;
    }

    @Override
    public int getHurtEnemyCost(LivingEntity livingEntity, ItemStack stack, LivingEntity enemy) {
        return super.getHurtEnemyCost(livingEntity, stack, enemy) * 2;
    }

    @Override
    public int getMineBlockCost(LivingEntity livingEntity, ItemStack stack, BlockState state) {
        return Math.max(1, super.getMineBlockCost(livingEntity, stack, state) / 2);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return true;
    }
}
