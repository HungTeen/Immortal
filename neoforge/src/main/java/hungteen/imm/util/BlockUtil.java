package hungteen.imm.util;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.common.block.artifacts.WoolCushionBlock;
import hungteen.imm.common.block.plants.GourdGrownBlock;
import hungteen.imm.common.tag.IMMBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-27 14:35
 **/
public class BlockUtil {

    public static void playSound(Level level, BlockPos pos, SoundEvent soundEvent){
        level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public static AABB getBlockAABB(BlockPos pos){
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    public static boolean canDestroy(Level level, BlockPos pos){
        return level.getBlockState(pos).getDestroySpeed(level, pos) >= 0;
    }

    public static boolean isCushion(Level level, BlockPos pos){
        return level.getBlockState(pos).is(IMMBlockTags.CUSHIONS);
    }

    /**
     * 麻将害我debug一晚上！！！
     */
    @Nullable
    public static BlockPattern.BlockPatternMatch match(Level level, BlockPattern pattern, BlockPos blockPos){
        final int size = Math.max(pattern.getDepth(), Math.max(pattern.getWidth(), pattern.getHeight()));
        for(int i = 0; i < 4; ++ i){
            final int mx = (i & 1) == 1 ? 0 : -1;
            final int mz = ((i >> 1) & 1) == 1 ? 0 : -1;
            final BlockPattern.BlockPatternMatch patternMatch = pattern.find(level, blockPos.offset(mx * size, 0, mz * size));
            if(patternMatch != null){
                return patternMatch;
            }
        }
        return null;
    }

    public static List<Pair<ResourceLocation, Block>> getWoolCushions(){
        return Arrays.stream(DyeColor.values()).map(WoolCushionBlock::getWoolCushionLocation)
                .map(res -> Pair.of(res, BlockHelper.get().get(res)))
                .filter(pair -> pair.getSecond().isPresent())
                .map(pair -> pair.mapSecond(Optional::get))
                .toList();
    }

    public static List<Pair<GourdGrownBlock.GourdTypes, GourdGrownBlock>> getGourds(){
        return Arrays.stream(GourdGrownBlock.GourdTypes.values())
                .map(res -> Pair.of(res, res.getGourdGrownBlock()))
                .toList();
    }
}
