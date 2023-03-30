package hungteen.imm.util;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.registry.BlockHelper;
import hungteen.imm.common.block.WoolCushionBlock;
import hungteen.imm.common.block.plants.GourdGrownBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 14:35
 **/
public class BlockUtil {

    public static void playSound(Level level, BlockPos pos, SoundEvent soundEvent){
        level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public static AABB getBlockAABB(BlockPos pos){
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    public static List<Pair<ResourceLocation, Block>> getWoolCushions(){
        return Arrays.stream(DyeColor.values()).map(WoolCushionBlock::getWoolCushionLocation)
                .map(res -> Pair.of(res, BlockHelper.get().get(res)))
                .filter(pair -> pair.getSecond().isPresent())
                .map(pair -> pair.mapSecond(Optional::get))
                .toList();
    }

    public static List<Pair<ResourceLocation, Block>> getGourds(){
        return Arrays.stream(GourdGrownBlock.GourdTypes.values()).map(GourdGrownBlock::getGourdLocation)
                .map(res -> Pair.of(res, BlockHelper.get().get(res)))
                .filter(pair -> pair.getSecond().isPresent())
                .map(pair -> pair.mapSecond(Optional::get))
                .toList();
    }
}
