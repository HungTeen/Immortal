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
    private static BlockPattern CreeperPattern;
    private static BlockPattern TeleportPattern;

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
                    .aisle("  ^  ", "     ", "^   ^", "     ", "  ^  ")
                    .aisle("#####", "#####", "#####", "#####", "#####")
                    .where('^', BlockInWorld.hasState(TeleportAnchorBlock.ANCHOR_PREDICATE))
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.REINFORCED_DEEPSLATE)))
                    .build();
        }
        return TeleportPattern;
    }
}
