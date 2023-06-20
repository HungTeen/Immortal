package hungteen.imm.common.block.artifacts;

import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.block.IMMBlockPatterns;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.block.IMMStateProperties;
import hungteen.imm.common.entity.misc.formation.TeleportFormation;
import hungteen.imm.common.tag.IMMItemTags;
import hungteen.imm.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-30 20:17
 **/
public class TeleportAnchorBlock extends Block {

    public static final Predicate<BlockState> ANCHOR_PREDICATE = (state) -> {
        return state != null && state.is(IMMBlocks.TELEPORT_ANCHOR.get()) && isFullCharged(state);
    };
    public static final IntegerProperty CHARGE = IMMStateProperties.TELEPORT_ANCHOR_CHARGES;

    public TeleportAnchorBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.REINFORCED_DEEPSLATE));
        this.registerDefaultState(this.stateDefinition.any().setValue(CHARGE, 0));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        final ItemStack itemstack = player.getItemInHand(hand);
        if (hand == InteractionHand.MAIN_HAND && !isTeleportFuel(itemstack) && isTeleportFuel(player.getItemInHand(InteractionHand.OFF_HAND))) {
            return InteractionResult.PASS;
        } else if (isTeleportFuel(itemstack) && canBeCharged(blockState)) {
            charge(level, pos, blockState);
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (blockState.getValue(CHARGE) == 0) {
            return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if (state.getValue(CHARGE) != 0) {
            if (randomSource.nextInt(100) == 0) {
                BlockUtil.playSound(level, pos, SoundEvents.RESPAWN_ANCHOR_AMBIENT);
            }

            final double x = pos.getX() + 0.5D + (0.5D - randomSource.nextDouble());
            final double y = pos.getY() + 1.1D;
            final double z = pos.getZ() + 0.5D + (0.5D - randomSource.nextDouble());
            ParticleHelper.spawnRandomSpeedParticle(level, IMMParticles.SPIRIT.get(), new Vec3(x, y, z),0.06F, 0.05F);
        }
    }

    public static void charge(Level level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(CHARGE, state.getValue(CHARGE) + 1), 3);
        BlockUtil.playSound(level, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE);
        if(! level.isClientSide){
            BlockPattern.BlockPatternMatch match = IMMBlockPatterns.getTeleportPattern().find(level, pos);
            if(match != null){
                final BlockPos center = match.getBlock(2, 1, 2).getPos();
                TeleportFormation formation = new TeleportFormation(level, center, 400);
                level.addFreshEntity(formation);
                Stream.of(
                        match.getBlock(0, 0, 0),
                        match.getBlock(4, 0, 0),
                        match.getBlock(0, 0, 4),
                        match.getBlock(4, 0, 4)
                ).forEach(blockInWorld -> {
                    if(blockInWorld.getState().is(IMMBlocks.TELEPORT_ANCHOR.get())){
                        level.setBlock(blockInWorld.getPos(), blockInWorld.getState().setValue(CHARGE, 0), 3);
                    }
                });
            }
        }
    }

    private static boolean isTeleportFuel(ItemStack itemStack) {
        return itemStack.is(IMMItemTags.SPIRITUAL_STONES);
    }

    private static boolean canBeCharged(BlockState state) {
        return ! isFullCharged(state);
    }

    private static boolean isFullCharged(BlockState state) {
        return state.getValue(CHARGE) == 4;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARGE);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
        return false;
    }
}
