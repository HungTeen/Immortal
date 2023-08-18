package hungteen.imm.common.world.levelgen;

import hungteen.htlib.util.helper.registry.LevelHelper;
import hungteen.imm.common.world.levelgen.dimension.EastWorldDimension;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-17 10:55
 **/
public interface IMMLevelStems {

    ResourceKey<LevelStem> EAST_WORLD = create("east_world");

    static void register(BootstapContext<LevelStem> context){
        EastWorldDimension.initLevelStem(context);
    }

    static ResourceKey<LevelStem> create(String name) {
        return LevelHelper.stem().createKey(Util.prefix(name));
    }

}
