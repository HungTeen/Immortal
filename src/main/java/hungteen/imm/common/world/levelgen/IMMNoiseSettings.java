package hungteen.imm.common.world.levelgen;

import hungteen.htlib.util.helper.registry.LevelHelper;
import hungteen.imm.common.world.levelgen.dimension.EastWorldDimension;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 19:38
 **/
public interface IMMNoiseSettings {

    ResourceKey<NoiseGeneratorSettings> EAST_WORLD = create("east_world");

    static void register(BootstapContext<NoiseGeneratorSettings> context){
        context.register(IMMNoiseSettings.EAST_WORLD, new NoiseGeneratorSettings(
                NoiseSettings.create(-64, 384, 1, 2),
                Blocks.STONE.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                IMMNoiseRouters.overworld(
                        context.lookup(Registries.DENSITY_FUNCTION),
                        context.lookup(Registries.NOISE)
                ),
                IMMSurfaceRules.overworldLike(false, true),
                EastWorldDimension.spawnTarget(),
                63,
                false,
                true,
                true,
                false
        ));
    }

    static ResourceKey<NoiseGeneratorSettings> create(String name) {
        return LevelHelper.noise().createKey(Util.prefix(name));
    }
}
