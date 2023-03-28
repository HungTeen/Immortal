package hungteen.imm.common.block;

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
public class ImmortalBlockPatterns {
    public static final Predicate<BlockState> PUMPKINS_PREDICATE = (state) -> state != null && (state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN));
    private static BlockPattern CreeperPattern;

    public static BlockPattern getCreeperPattern(){
        if(CreeperPattern == null){
            CreeperPattern = BlockPatternBuilder.start().aisle("^", "#", "#").where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.TNT))).build();
        }
        return CreeperPattern;
    }
}
