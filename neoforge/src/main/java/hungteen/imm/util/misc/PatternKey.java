package hungteen.imm.util.misc;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.function.Predicate;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/9/27 21:50
 **/
public record PatternKey(char key, Predicate<BlockInWorld> predicate, BlockState defaultState) {
}
