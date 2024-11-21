package hungteen.imm.common.block.artifacts;

import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.block.IMMBlockPatterns;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.block.IMMStateProperties;
import hungteen.imm.common.entity.misc.formation.TeleportFormation;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.tag.IMMItemTags;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
 * @program Immortal
 * @author HungTeen
 * @create 2023-03-30 20:17
 **/
public class TeleportAnchorBlock extends SimpleArtifactBlock {

    public static final Predicate<BlockState> ANCHOR_PREDICATE = (state) -> {
        return state != null && state.is(IMMBlocks.TELEPORT_ANCHOR.get()) && isFullCharged(state);
    };
    public static final IntegerProperty CHARGE = IMMStateProperties.TELEPORT_ANCHOR_CHARGES;

    public static BlockState fullCharged(){
        return IMMBlocks.TELEPORT_ANCHOR.get().defaultBlockState().setValue(CHARGE, 4);
    }

    public TeleportAnchorBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.REINFORCED_DEEPSLATE), RealmTypes.ADVANCED_ARTIFACT);
        this.registerDefaultState(this.stateDefinition.any().setValue(CHARGE, 0));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemstack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand == InteractionHand.MAIN_HAND && !isTeleportFuel(itemstack) && isTeleportFuel(player.getItemInHand(InteractionHand.OFF_HAND))) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        } else if (isTeleportFuel(itemstack) && canBeCharged(state)) {
            charge(level, pos, state);
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            return ItemInteractionResult.CONSUME;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if (state.getValue(CHARGE) != 0) {
            if (randomSource.nextInt(100) == 0) {
                BlockUtil.playSound(level, pos, SoundEvents.RESPAWN_ANCHOR_AMBIENT);
            }
            final double speedX = randomSource.nextGaussian() * 0.06;
            final double speedY = randomSource.nextFloat() * 0.05;
            final double speedZ = randomSource.nextGaussian() * 0.06;
            ParticleUtil.spawnParticleOnFace(level, pos, Direction.UP, IMMParticles.SPIRITUAL_MANA.get(), 1, new Vec3(speedX, speedY, speedZ), 0);
        }
    }

    public static void charge(Level level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(CHARGE, state.getValue(CHARGE) + 1), 3);
        BlockUtil.playSound(level, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE);
        if(! level.isClientSide){
            BlockPattern.BlockPatternMatch match = IMMBlockPatterns.getTeleportPattern().blockPattern().find(level, pos);
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
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

}
