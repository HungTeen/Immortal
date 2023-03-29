package hungteen.imm.common.world.levelgen;

import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/29 15:05
 */
public class IMMDimensionTypes {

    public static final ResourceKey<DimensionType> SPIRITUAL_LAND = create("spiritual_land");

    public static void register(BootstapContext<DimensionType> context){
    }

    private static ResourceKey<DimensionType> create(String name) {
        return ResourceKey.create(Registries.DIMENSION_TYPE, Util.prefix(name));
    }
}
