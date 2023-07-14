package hungteen.imm.common.world.levelgen.features;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;

import java.util.OptionalInt;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/14 14:31
 */
public class IMMTreeFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_DARK_OAK = IMMFeatures.create("tall_dark_oak");

    public static void register(BootstapContext<ConfiguredFeature<?, ?>> context) {
        FeatureUtils.register(context, TALL_DARK_OAK, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(Blocks.DARK_OAK_LOG),
                        new DarkOakTrunkPlacer(9, 4, 3),
                        BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES),
                        new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                        new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
                ).ignoreVines().build()
        );
    }

}
