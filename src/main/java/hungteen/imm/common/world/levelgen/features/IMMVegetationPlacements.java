package hungteen.imm.common.world.levelgen.features;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-29 22:36
 **/
public class IMMVegetationPlacements {

    public static final ResourceKey<PlacedFeature> OAK_STAKE = IMMPlacements.create("oak_stake");
    public static final ResourceKey<PlacedFeature> OAK_HORIZONTAL_STAKE = IMMPlacements.create("oak_horizontal_stake");
    public static final ResourceKey<PlacedFeature> GANODERMA_DARK_FOREST = IMMPlacements.create("ganoderma_dark_forest");

    public static void register(BootstapContext<PlacedFeature> context) {
        final HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        final Holder<ConfiguredFeature<?, ?>> oakStake = features.getOrThrow(IMMVegetationFeatures.OAK_STAKE);
        final Holder<ConfiguredFeature<?, ?>> oakHorizontalStake = features.getOrThrow(IMMVegetationFeatures.OAK_HORIZONTAL_STAKE);
        final Holder<ConfiguredFeature<?, ?>> ganoderma = features.getOrThrow(IMMVegetationFeatures.PATCH_GANODERMA);
        final PlacementModifier placementmodifier = SurfaceWaterDepthFilter.forMaxDepth(0);
        PlacementUtils.register(context, OAK_STAKE, oakStake,
                PlacementUtils.countExtra(0, 0.05F, 1),
                InSquarePlacement.spread(),
                placementmodifier,
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome()
        );
        PlacementUtils.register(context, OAK_HORIZONTAL_STAKE, oakHorizontalStake,
                PlacementUtils.countExtra(0, 0.05F, 1),
                InSquarePlacement.spread(),
                placementmodifier,
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome()
        );
        PlacementUtils.register(context, GANODERMA_DARK_FOREST, ganoderma,
                CountPlacement.of(2),
                RarityFilter.onAverageOnceEvery(8),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        );
    }

}
