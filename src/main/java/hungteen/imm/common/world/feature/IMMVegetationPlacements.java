package hungteen.imm.common.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-29 22:36
 **/
public interface IMMVegetationPlacements {

    ResourceKey<PlacedFeature> OAK_STAKE = IMMPlacements.create("oak_stake");
    ResourceKey<PlacedFeature> OAK_HORIZONTAL_STAKE = IMMPlacements.create("oak_horizontal_stake");
    ResourceKey<PlacedFeature> BIRCH_STAKE = IMMPlacements.create("birch_stake");
    ResourceKey<PlacedFeature> BIRCH_HORIZONTAL_STAKE = IMMPlacements.create("birch_horizontal_stake");
    ResourceKey<PlacedFeature> DARK_OAK_STAKE = IMMPlacements.create("dark_oak_stake");
    ResourceKey<PlacedFeature> DARK_OAK_HORIZONTAL_STAKE = IMMPlacements.create("dark_oak_horizontal_stake");
    ResourceKey<PlacedFeature> TREES_BIRCH_FOREST = IMMPlacements.create("trees_birch_forest");
    ResourceKey<PlacedFeature> TREES_DARK_FOREST = IMMPlacements.create("trees_dark_forest");
    ResourceKey<PlacedFeature> GANODERMA_DARK_FOREST = IMMPlacements.create("ganoderma_dark_forest");

    /**
     * {@link net.minecraft.data.worldgen.placement.VegetationPlacements#bootstrap(BootstapContext)}
     */
    static void register(BootstapContext<PlacedFeature> context) {
        final HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        final Holder<ConfiguredFeature<?, ?>> treesBirchForest = features.getOrThrow(IMMVegetationFeatures.TREES_BIRCH_FOREST);
        final Holder<ConfiguredFeature<?, ?>> treesDarkForest = features.getOrThrow(IMMVegetationFeatures.TREES_DARK_FOREST);
        final Holder<ConfiguredFeature<?, ?>> ganoderma = features.getOrThrow(IMMVegetationFeatures.PATCH_GANODERMA);
        final PlacementModifier placementmodifier = SurfaceWaterDepthFilter.forMaxDepth(0);
        stake(context, features, OAK_STAKE, IMMVegetationFeatures.OAK_STAKE, placementmodifier);
        stake(context, features, OAK_HORIZONTAL_STAKE, IMMVegetationFeatures.OAK_HORIZONTAL_STAKE, placementmodifier);
        stake(context, features, BIRCH_STAKE, IMMVegetationFeatures.BIRCH_STAKE, placementmodifier);
        stake(context, features, BIRCH_HORIZONTAL_STAKE, IMMVegetationFeatures.BIRCH_HORIZONTAL_STAKE, placementmodifier);
        stake(context, features, DARK_OAK_STAKE, IMMVegetationFeatures.DARK_OAK_STAKE, placementmodifier);
        stake(context, features, DARK_OAK_HORIZONTAL_STAKE, IMMVegetationFeatures.DARK_OAK_HORIZONTAL_STAKE, placementmodifier);
        PlacementUtils.register(context, TREES_BIRCH_FOREST, treesBirchForest, VegetationPlacements.treePlacement(PlacementUtils.countExtra(2, 0.1F, 1)));
        PlacementUtils.register(context, TREES_DARK_FOREST, treesDarkForest,
                CountPlacement.of(16),
                InSquarePlacement.spread(),
                placementmodifier,
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome()
        );
        PlacementUtils.register(context, GANODERMA_DARK_FOREST, ganoderma,
                CountPlacement.of(2),
                RarityFilter.onAverageOnceEvery(12),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        );
    }

    private static void stake(BootstapContext<PlacedFeature> context, HolderGetter<ConfiguredFeature<?, ?>> features, ResourceKey<PlacedFeature> placedKey, ResourceKey<ConfiguredFeature<?, ?>> configuredKey, PlacementModifier extraModifier){
        stake(context, placedKey, features.getOrThrow(configuredKey), extraModifier);
    }

    private static void stake(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> resourceKey, Holder<ConfiguredFeature<?, ?>> holder, PlacementModifier extraModifier){
        stake(context, resourceKey, holder, PlacementUtils.countExtra(0, 0.5F, 1), extraModifier);
    }

    private static void stake(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> resourceKey, Holder<ConfiguredFeature<?, ?>> holder, PlacementModifier countModifier, PlacementModifier extraModifier){
        PlacementUtils.register(context, resourceKey, holder,
                countModifier,
                InSquarePlacement.spread(),
                extraModifier,
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
        );
    }

}
