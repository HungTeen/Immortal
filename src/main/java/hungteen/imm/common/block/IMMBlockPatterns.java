package hungteen.imm.common.block;

import hungteen.imm.common.block.artifacts.TeleportAnchorBlock;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.util.misc.HTBlockPattern;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraftforge.event.level.ExplosionEvent;

import java.util.function.Predicate;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/14 18:19
 */
public class IMMBlockPatterns {

    public static final Predicate<BlockState> PUMPKINS_PREDICATE = (state) -> state != null && (state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN));
    public static final Predicate<BlockState> COPPER_SLAB_PREDICATE = state -> state.is(Blocks.CUT_COPPER_SLAB) || state.is(Blocks.EXPOSED_CUT_COPPER_SLAB) || state.is(Blocks.WEATHERED_CUT_COPPER_SLAB) || state.is(Blocks.OXIDIZED_CUT_COPPER_SLAB);
    public static final Predicate<BlockState> COPPER_BLOCK_PREDICATE = state -> state.is(Blocks.COPPER_BLOCK) || state.is(Blocks.EXPOSED_COPPER) || state.is(Blocks.WEATHERED_COPPER) || state.is(Blocks.OXIDIZED_COPPER);
    private static HTBlockPattern CreeperPattern;
    private static HTBlockPattern TeleportPattern;
    private static HTBlockPattern FurnacePattern;

    public static HTBlockPattern getCreeperPattern(){
        if(CreeperPattern == null){
            CreeperPattern = HTBlockPattern.builder()
                    .aisle("^", "#", "#")
                    .where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE))
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.TNT)))
                    .build();
        }
        return CreeperPattern;
    }

    public static HTBlockPattern getTeleportPattern(){
        if(TeleportPattern == null){
            TeleportPattern = HTBlockPattern.builder()
                    .aisle("^   ^", "#   #")
                    .aisle("     ", "     ")
                    .aisle("     ", "     ")
                    .aisle("     ", "     ")
                    .aisle("^   ^", "#   #")
                    .key('^', BlockInWorld.hasState(TeleportAnchorBlock.ANCHOR_PREDICATE), IMMBlocks.TELEPORT_ANCHOR.get().defaultBlockState())
                    .key('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.POLISHED_DEEPSLATE)), Blocks.POLISHED_DEEPSLATE.defaultBlockState())
                    .build();
        }
        return TeleportPattern;
    }

    public static HTBlockPattern getFurnacePattern(){
        if(FurnacePattern == null){
            FurnacePattern = furnace(
                    BlockInWorld.hasState(state -> state.is(IMMBlockTags.COPPER_SLABS)),
                    BlockInWorld.hasState(state -> state.is(IMMBlockTags.COPPER_BLOCKS)),
                    BlockInWorld.hasState(BlockState::isAir),
                    BlockInWorld.hasState(BlockStatePredicate.forBlock(IMMBlocks.COPPER_FURNACE.get())),
                    BlockInWorld.hasState(state -> state.is(IMMBlockTags.COPPER_BLOCKS))
            );
        }
        return FurnacePattern;
    }

    private static HTBlockPattern furnace(Predicate<BlockInWorld> slab, Predicate<BlockInWorld> block, Predicate<BlockInWorld> air, Predicate<BlockInWorld> furnace, Predicate<BlockInWorld> output){
        return HTBlockPattern.builder()
                .aisle("       ", "       ", "       ", "       ", " #   # ")
                .aisle("   ^   ", "  ###  ", "  #i#  ", "  ###  ", "       ")
                .aisle("  ^ ^  ", "# ### #", " ##a## ", "  #i#  ", "       ")
                .aisle("   ^   ", "  ###  ", "  #f#  ", "  ###  ", "       ")
                .aisle("       ", "       ", "       ", "       ", " #   # ")
                .key('^', slab, Blocks.CUT_COPPER_SLAB.defaultBlockState())
                .key('#', block, Blocks.COPPER_BLOCK.defaultBlockState())
                .where('a', air)
                .key('f', furnace, Blocks.COPPER_BLOCK.defaultBlockState())
                .key('i', output, Blocks.COPPER_BLOCK.defaultBlockState())
                .build();
    }
}
