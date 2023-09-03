package hungteen.imm.common.world.levelgen.features;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.util.List;
import java.util.OptionalInt;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/14 14:31
 */
public interface IMMTreeFeatures {

    ResourceKey<ConfiguredFeature<?, ?>> TALL_BIRCH_TREE = IMMFeatures.create("tall_birch_tree");
    ResourceKey<ConfiguredFeature<?, ?>> TALL_BIRCH_TREE_WITH_BEES = IMMFeatures.create("tall_birch_tree_with_bees");
    ResourceKey<ConfiguredFeature<?, ?>> TALL_DARK_OAK = IMMFeatures.create("tall_dark_oak");

    /**
     * {@link net.minecraft.data.worldgen.features.TreeFeatures}
     */
    static void register(BootstapContext<ConfiguredFeature<?, ?>> context) {
        final BeehiveDecorator beehiveDecorator = new BeehiveDecorator(0.02F);
        FeatureUtils.register(context, TALL_BIRCH_TREE, Feature.TREE, createStraightBlobTree(
                Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, 6, 2, 3, 2
                ).ignoreVines().build()
        );
        FeatureUtils.register(context, TALL_BIRCH_TREE_WITH_BEES, Feature.TREE, createStraightBlobTree(
                        Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, 7, 3, 4, 3
                ).ignoreVines().decorators(List.of(beehiveDecorator)).build()
        );
        FeatureUtils.register(context, TALL_DARK_OAK, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(Blocks.DARK_OAK_LOG),
                        new DarkOakTrunkPlacer(9, 4, 3),
                        BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES),
                        new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                        new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
                ).ignoreVines().build()
        );
    }

    private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobTree(Block log, Block leaves, int baseH, int h1, int h2, int radius) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(log),
                new StraightTrunkPlacer(baseH, h1, h2),
                BlockStateProvider.simple(leaves),
                new BlobFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        );
    }

}
