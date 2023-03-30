package hungteen.imm.common.world.levelgen;

import hungteen.htlib.util.helper.registry.LevelHelper;
import hungteen.imm.common.world.levelgen.dimension.EastWorldDimension;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 19:38
 **/
public class IMMNoiseSettings {

    public static final ResourceKey<NoiseGeneratorSettings> EAST_WORLD = create("east_world");

    public static void register(BootstapContext<NoiseGeneratorSettings> context){
        EastWorldDimension.initNoiseSettings(context);
    }

    private static ResourceKey<NoiseGeneratorSettings> create(String name) {
        return LevelHelper.noise().createKey(Util.prefix(name));
    }
}
