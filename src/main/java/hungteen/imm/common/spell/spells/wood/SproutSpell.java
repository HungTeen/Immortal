package hungteen.imm.common.spell.spells.wood;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.spell.spells.SpellType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/18 14:25
 */
public class SproutSpell extends SpellType {

    public SproutSpell() {
        super("sprout", properties().maxLevel(1).mana(50).cd(400));
    }

    /**
     * {@link BoneMealItem#useOn(UseOnContext)}.
     */
    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        boolean success = false;
        if (result.hasBlock() && result.getBlockPos() != null && result.getDirection() != null) {
            final BlockPos blockpos = result.getBlockPos();
            final BlockPos blockpos1 = blockpos.relative(result.getDirection());
            if (applySprout(owner.level(), blockpos)) {
                if (! owner.level().isClientSide()) {
                    owner.level().levelEvent(1505, blockpos, 0);
                }

                success = true;
            } else {
                final BlockState blockstate = owner.level().getBlockState(blockpos);
                final boolean flag = blockstate.isFaceSturdy(owner.level(), blockpos, result.getDirection());
                if (flag && BoneMealItem.growWaterPlant(ItemStack.EMPTY, owner.level(), blockpos1, result.getDirection())) {
                    if (! owner.level().isClientSide()) {
                        owner.level().levelEvent(1505, blockpos1, 0);
                    }
                    success = true;
                }
            }
            if(success){
                ParticleHelper.spawnLineMovingParticle(owner.level(), IMMParticles.SPIRITUAL_MANA.get(), owner.getEyePosition(), MathHelper.toVec3(blockpos), 1, 0.1, 0.1);
            }
        }
        return success;
    }

    public static boolean applySprout(Level level, BlockPos pos) {
        final BlockState blockstate = level.getBlockState(pos);
        if (blockstate.getBlock() instanceof BonemealableBlock block) {
            if (block.isValidBonemealTarget(level, pos, blockstate, level.isClientSide)) {
                if (level instanceof ServerLevel) {
                    if (block.isBonemealSuccess(level, level.random, pos, blockstate)) {
                        block.performBonemeal((ServerLevel)level, level.random, pos, blockstate);
                    }
                }
                return true;
            }
        }
        return false;
    }

}
