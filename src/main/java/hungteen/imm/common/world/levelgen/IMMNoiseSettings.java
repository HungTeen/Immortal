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
public interface IMMNoiseSettings {

    ResourceKey<NoiseGeneratorSettings> EAST_WORLD = create("east_world");

    static void register(BootstapContext<NoiseGeneratorSettings> context){
        EastWorldDimension.initNoiseSettings(context);
    }

    static ResourceKey<NoiseGeneratorSettings> create(String name) {
        return LevelHelper.noise().createKey(Util.prefix(name));
    }
}
