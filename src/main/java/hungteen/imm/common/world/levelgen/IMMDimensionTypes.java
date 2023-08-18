package hungteen.imm.common.world.levelgen;

import hungteen.htlib.util.helper.registry.LevelHelper;
import hungteen.imm.common.world.levelgen.dimension.EastWorldDimension;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/29 15:05
 */
public interface IMMDimensionTypes {

    public static final ResourceKey<DimensionType> EAST_WORLD = create("east_world");

    public static void register(BootstapContext<DimensionType> context){
        EastWorldDimension.initDimensionType(context);
    }

    private static ResourceKey<DimensionType> create(String name) {
        return LevelHelper.type().createKey(Util.prefix(name));
    }
}
