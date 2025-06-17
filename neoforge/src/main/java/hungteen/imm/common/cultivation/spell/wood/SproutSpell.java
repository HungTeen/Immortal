package hungteen.imm.common.cultivation.spell.wood;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.cultivation.spell.RequireEmptyHandSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 14:25
 */
public class SproutSpell extends RequireEmptyHandSpell {

    public SproutSpell() {
        super("sprout", property().maxLevel(1).mana(50).cd(400));
    }

    /**
     * {@link BoneMealItem#useOn(UseOnContext)}.
     */
    @Override
    public boolean checkActivate(SpellCastContext context, InteractionHand hand) {
        boolean success = false;
        if(context.targetStateOpt().isPresent()){
            final BlockPos blockpos = context.targetPos();
            final BlockPos blockpos1 = blockpos.relative(context.direction());
            if (applySprout(context.level(), blockpos)) {
                if (! context.level().isClientSide()) {
                    context.level().levelEvent(1505, blockpos, 0);
                }

                success = true;
            } else {
                final BlockState blockstate = context.level().getBlockState(blockpos);
                final boolean flag = blockstate.isFaceSturdy(context.level(), blockpos, context.direction());
                if (flag && BoneMealItem.growWaterPlant(ItemStack.EMPTY, context.level(), blockpos1, context.direction())) {
                    if (! context.level().isClientSide()) {
                        context.level().levelEvent(1505, blockpos1, 0);
                    }
                    success = true;
                }
            }
            if(success){
                ParticleHelper.spawnLineMovingParticle(context.level(), IMMParticles.QI.get(), context.owner().getEyePosition(), MathHelper.toVec3(blockpos), 1, 0.1, 0.1);
            }
        }
        return success;
    }

    public static boolean applySprout(Level level, BlockPos pos) {
        final BlockState blockstate = level.getBlockState(pos);
        if (blockstate.getBlock() instanceof BonemealableBlock block) {
            if (block.isValidBonemealTarget(level, pos, blockstate)) {
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
