package hungteen.imm.common.world.levelgen.features;

import hungteen.htlib.util.helper.registry.FeatureHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.world.levelgen.features.configuration.HorizontalStakeConfiguration;
import hungteen.imm.common.world.levelgen.features.configuration.WoodStakeConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-29 22:36
 **/
public interface IMMVegetationFeatures {

    ResourceKey<ConfiguredFeature<?, ?>> OAK_STAKE = IMMFeatures.create("oak_stake");
    ResourceKey<ConfiguredFeature<?, ?>> OAK_HORIZONTAL_STAKE = IMMFeatures.create("oak_horizontal_stake");
    ResourceKey<ConfiguredFeature<?, ?>> BIRCH_STAKE = IMMFeatures.create("birch_stake");
    ResourceKey<ConfiguredFeature<?, ?>> BIRCH_HORIZONTAL_STAKE = IMMFeatures.create("birch_horizontal_stake");
    ResourceKey<ConfiguredFeature<?, ?>> TREES_BIRCH_FOREST = IMMFeatures.create("trees_birch_forest");
    ResourceKey<ConfiguredFeature<?, ?>> PATCH_GANODERMA = FeatureUtils.createKey("patch_ganoderma");

    /**
     * {@link net.minecraft.data.worldgen.features.VegetationFeatures#bootstrap(BootstapContext)}
     */
    static void register(BootstapContext<ConfiguredFeature<?, ?>> context) {
        final HolderGetter<PlacedFeature> placedFeatures = FeatureHelper.placed().lookup(context);
        final Holder<PlacedFeature> tallBirch = placedFeatures.getOrThrow(IMMTreePlacements.TALL_BIRCH_CHECKED);
        final Holder<PlacedFeature> tallBirchWithBees = placedFeatures.getOrThrow(IMMTreePlacements.TALL_BIRCH_WITH_BEES_CHECKED);
        FeatureUtils.register(context, OAK_STAKE, IMMFeatures.WOOD_STAKE.get(), new WoodStakeConfiguration(
                UniformInt.of(1, 3), SimpleStateProvider.simple(Blocks.OAK_LOG.defaultBlockState())
        ));
        FeatureUtils.register(context, OAK_HORIZONTAL_STAKE, IMMFeatures.HORIZONTAL_STAKE.get(), new HorizontalStakeConfiguration(
                UniformInt.of(3, 9), UniformInt.of(1, 5),
                SimpleStateProvider.simple(Blocks.OAK_LOG.defaultBlockState())
        ));
        FeatureUtils.register(context, BIRCH_STAKE, IMMFeatures.WOOD_STAKE.get(), new WoodStakeConfiguration(
                UniformInt.of(1, 3), SimpleStateProvider.simple(Blocks.BIRCH_LOG.defaultBlockState())
        ));
        FeatureUtils.register(context, BIRCH_HORIZONTAL_STAKE, IMMFeatures.HORIZONTAL_STAKE.get(), new HorizontalStakeConfiguration(
                UniformInt.of(3, 9), UniformInt.of(1, 3),
                SimpleStateProvider.simple(Blocks.BIRCH_LOG.defaultBlockState())
        ));
        FeatureUtils.register(context, TREES_BIRCH_FOREST, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
                List.of(new WeightedPlacedFeature(tallBirch, 0.1F)),
                tallBirchWithBees
        ));
        FeatureUtils.register(context, PATCH_GANODERMA, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
                Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(IMMBlocks.GANODERMA.get())))
        );
    }

}
