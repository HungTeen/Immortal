package hungteen.imm.common.block;

import hungteen.imm.common.block.artifacts.TeleportAnchorBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

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
    private static BlockPattern CreeperPattern;
    private static BlockPattern TeleportPattern;
    private static BlockPattern FurnacePattern;

    public static BlockPattern getCreeperPattern(){
        if(CreeperPattern == null){
            CreeperPattern = BlockPatternBuilder.start()
                    .aisle("^", "#", "#")
                    .where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE))
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.TNT)))
                    .build();
        }
        return CreeperPattern;
    }

    public static BlockPattern getTeleportPattern(){
        if(TeleportPattern == null){
            TeleportPattern = BlockPatternBuilder.start()
                    .aisle("^   ^", "     ", "     ", "     ", "^   ^")
                    .aisle("#   #", "     ", "     ", "     ", "#   #")
                    .where('^', BlockInWorld.hasState(TeleportAnchorBlock.ANCHOR_PREDICATE))
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.POLISHED_DEEPSLATE)))
                    .build();
        }
        return TeleportPattern;
    }

    public static BlockPattern getFurnacePattern(){
        if(FurnacePattern == null){
            FurnacePattern = furnace(
                    BlockInWorld.hasState(state ->
                            state.is(Blocks.CUT_COPPER_SLAB) || state.is(Blocks.EXPOSED_CUT_COPPER_SLAB) || state.is(Blocks.WEATHERED_CUT_COPPER_SLAB) || state.is(Blocks.OXIDIZED_CUT_COPPER_SLAB)
                    ),
                    BlockInWorld.hasState(state ->
                            state.is(Blocks.COPPER_BLOCK) || state.is(Blocks.EXPOSED_COPPER) || state.is(Blocks.WEATHERED_COPPER) || state.is(Blocks.OXIDIZED_COPPER)
                    ),
                    BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.COPPER_BLOCK)),
                    BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.COPPER_BLOCK))
            );
        }
        return FurnacePattern;
    }

    private static BlockPattern furnace(Predicate<BlockInWorld> top, Predicate<BlockInWorld> basis, Predicate<BlockInWorld> functional, Predicate<BlockInWorld> fuel){
        return BlockPatternBuilder.start()
                .aisle("       ", "       ", "   ^   ", "  ^x^  ", "   ^   ", "       ", "       ")
                .aisle("       ", "       ", "  ###  ", "# # # #", "  ###  ", "       ", "       ")
                .aisle("       ", "       ", "  ###  ", " ## ## ", "  ###  ", "       ", "       ")
                .aisle("       ", "       ", "  ###  ", "  #y#  ", "  ###  ", "       ", "       ")
                .aisle("       ", "       ", " #   # ", "       ", " #   # ", "       ", "       ")
                .where('^', top)
                .where('#', basis)
                .where('x', functional)
                .where('y', fuel)
                .build();
    }
}
