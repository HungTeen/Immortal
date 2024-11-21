package hungteen.imm.common.world.feature;

import hungteen.htlib.util.helper.impl.FeatureHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.world.feature.configuration.HorizontalStakeConfiguration;
import hungteen.imm.common.world.feature.configuration.WoodStakeConfiguration;
import hungteen.imm.util.FeatureUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
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
 * @program Immortal
 * @author HungTeen
 * @create 2023-06-29 22:36
 **/
public interface IMMVegetationFeatures {

    ResourceKey<ConfiguredFeature<?, ?>> OAK_STAKE = IMMFeatures.create("oak_stake");
    ResourceKey<ConfiguredFeature<?, ?>> OAK_HORIZONTAL_STAKE = IMMFeatures.create("oak_horizontal_stake");
    ResourceKey<ConfiguredFeature<?, ?>> BIRCH_STAKE = IMMFeatures.create("birch_stake");
    ResourceKey<ConfiguredFeature<?, ?>> BIRCH_HORIZONTAL_STAKE = IMMFeatures.create("birch_horizontal_stake");
    ResourceKey<ConfiguredFeature<?, ?>> DARK_OAK_STAKE = IMMFeatures.create("dark_oak_stake");
    ResourceKey<ConfiguredFeature<?, ?>> DARK_OAK_HORIZONTAL_STAKE = IMMFeatures.create("dark_oak_horizontal_stake");
    ResourceKey<ConfiguredFeature<?, ?>> TREES_BIRCH_FOREST = IMMFeatures.create("trees_birch_forest");
    ResourceKey<ConfiguredFeature<?, ?>> TREES_DARK_FOREST = IMMFeatures.create("trees_dark_forest");
    ResourceKey<ConfiguredFeature<?, ?>> PATCH_GANODERMA = FeatureUtils.createKey("patch_ganoderma");

    /**
     * {@link net.minecraft.data.worldgen.features.VegetationFeatures#bootstrap(BootstrapContext)}
     */
    static void register(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        final HolderGetter<ConfiguredFeature<?, ?>> features = FeatureHelper.config().lookup(context);
        final HolderGetter<PlacedFeature> placedFeatures = FeatureHelper.placed().lookup(context);
        Holder<ConfiguredFeature<?, ?>> brownMushroom = features.getOrThrow(TreeFeatures.HUGE_BROWN_MUSHROOM);
        Holder<ConfiguredFeature<?, ?>> redMushroom = features.getOrThrow(TreeFeatures.HUGE_RED_MUSHROOM);
        final Holder<PlacedFeature> fancyOak = placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_CHECKED);
        final Holder<PlacedFeature> tallBirch = placedFeatures.getOrThrow(IMMTreePlacements.TALL_BIRCH_CHECKED);
        final Holder<PlacedFeature> tallBirchWithBees = placedFeatures.getOrThrow(IMMTreePlacements.TALL_BIRCH_WITH_BEES_CHECKED);
        final Holder<PlacedFeature> darkOak = placedFeatures.getOrThrow(TreePlacements.DARK_OAK_CHECKED);
        final Holder<PlacedFeature> tallDarkOak = placedFeatures.getOrThrow(IMMTreePlacements.TALL_DARK_OAK_CHECKED);
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
        FeatureUtils.register(context, DARK_OAK_STAKE, IMMFeatures.WOOD_STAKE.get(), new WoodStakeConfiguration(
                UniformInt.of(1, 3), SimpleStateProvider.simple(Blocks.OAK_LOG.defaultBlockState())
        ));
        FeatureUtils.register(context, DARK_OAK_HORIZONTAL_STAKE, IMMFeatures.HORIZONTAL_STAKE.get(), new HorizontalStakeConfiguration(
                UniformInt.of(3, 9), UniformInt.of(1, 5),
                SimpleStateProvider.simple(Blocks.DARK_OAK_LOG.defaultBlockState())
        ));
        FeatureUtils.register(context, TREES_BIRCH_FOREST, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
                List.of(new WeightedPlacedFeature(tallBirch, 0.1F)),
                tallBirchWithBees
        ));
        FeatureUtils.register(context, TREES_DARK_FOREST, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
                List.of(
                        new WeightedPlacedFeature(PlacementUtils.inlinePlaced(brownMushroom), 0.075F),
                        new WeightedPlacedFeature(PlacementUtils.inlinePlaced(redMushroom), 0.075F),
                        new WeightedPlacedFeature(tallDarkOak, 0.6F),
                        new WeightedPlacedFeature(darkOak, 0.15F)
                ), fancyOak
        ));
        FeatureUtils.register(context, PATCH_GANODERMA, Feature.RANDOM_PATCH, FeatureUtil.simplePatchConfiguration(
                Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(IMMBlocks.GANODERMA.get())), 32
        ));
    }

}
