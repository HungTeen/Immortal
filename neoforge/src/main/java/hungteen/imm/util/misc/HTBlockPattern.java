package hungteen.imm.util.misc;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.JavaHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/9/27 21:50
 **/
public class HTBlockPattern {

    private final List<String[]> pattern;
    private final Map<Character, BlockState> defaultStateMap = new HashMap<>();
    private final BlockPattern blockPattern;

    public HTBlockPattern(List<String[]> pattern, List<PatternKey> patternKeys) {
        final BlockPatternBuilder builder = BlockPatternBuilder.start();
        pattern.forEach(builder::aisle);
        patternKeys.forEach(k -> {
            builder.where(k.key(), k.predicate());
            this.defaultStateMap.put(k.key(), k.defaultState());
        });
        this.pattern = pattern;
        this.blockPattern = builder.build();
    }

    public BlockPattern blockPattern() {
        return blockPattern;
    }

    public int getDepth() {
        return blockPattern.getDepth();
    }

    public int getHeight() {
        return blockPattern.getHeight();
    }

    public int getWidth() {
        return blockPattern.getWidth();
    }

    public char getCharAt(int x, int y, int z) {
        return pattern.get(z)[y].charAt(x);
    }

    @Nullable
    public BlockState get(int x, int y, int z) {
        return get(getCharAt(x, y, z));
    }

    @Nullable
    public BlockState get(char key){
        return defaultStateMap.getOrDefault(key, null);
    }

    public List<Pair<BlockPos, BlockState>> getBlockStates(BlockPos pos, int x, int y, int z){
        return getBlockStates(pos, x, y, z, false);
    }

    /**
     * 根据输入的字符串Pattern，构建需要的多方块结构。
     * @param pos 中心点在世界中的真实位置。
     * @param x 中心点在字符串Pattern中的x坐标。
     * @param y 中心点在字符串Pattern中的y坐标。
     * @param z 中心点在字符串Pattern中的z坐标。
     * @param fillAir 其他地方是否强制填充为空气。
     * @return 一系列构成结构的方块。
     */
    public List<Pair<BlockPos, BlockState>> getBlockStates(BlockPos pos, int x, int y, int z, boolean fillAir) {
        final List<Pair<BlockPos, BlockState>> states = new ArrayList<>();
        for(int i = 0; i < getWidth(); i++) {
            for(int j = 0; j < getHeight(); ++ j){
                for(int k = 0; k < getDepth(); ++ k){
                    final BlockState state = get(i, j, k);
                    final BlockPos curPos = new BlockPos(pos.getX() + i - x, pos.getY() + y - j, pos.getZ() + k - z).immutable();
                    if(state != null) {
                        states.add(Pair.of(curPos, state));
                    } else if(fillAir){
                        states.add(Pair.of(curPos, Blocks.AIR.defaultBlockState()));
                    }
                }
            }
        }
        return states;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder {

        private final List<String[]> patterns = new ArrayList<>();
        private final List<PatternKey> patternKeys = new ArrayList<>();

        public Builder aisle(String... pattern) {
            this.patterns.add(pattern);
            return this;
        }

        public Builder key(char key, Predicate<BlockInWorld> predicate, BlockState state) {
            this.patternKeys.add(new PatternKey(key, predicate, state));
            return this;
        }

        public Builder where(char key, Predicate<BlockInWorld> predicate) {
            return key(key, predicate, Blocks.AIR.defaultBlockState());
        }

        public HTBlockPattern build() {
            return new HTBlockPattern(patterns, patternKeys);
        }
    }

}
