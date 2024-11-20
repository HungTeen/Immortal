package hungteen.imm.common.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/2 9:23
 */
public interface IMMOrePlacements {

    ResourceKey<PlacedFeature> ORE_EMERALD = IMMPlacements.create("ore_emerald");
    ResourceKey<PlacedFeature> ORE_EMERALD_SMALL = IMMPlacements.create("ore_emerald_small");
    ResourceKey<PlacedFeature> ORE_CINNABAR = IMMPlacements.create("ore_cinnabar");

    static void register(BootstrapContext<PlacedFeature> context) {
        final HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        final Holder<ConfiguredFeature<?, ?>> emeraldOre = features.getOrThrow(OreFeatures.ORE_EMERALD);
        final Holder<ConfiguredFeature<?, ?>> cinnabarOre = features.getOrThrow(IMMOreFeatures.ORE_CINNABAR);
        PlacementUtils.register(context, ORE_EMERALD, emeraldOre, commonOrePlacement(30, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480))));
        PlacementUtils.register(context, ORE_EMERALD_SMALL, emeraldOre, commonOrePlacement(20, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480))));
        PlacementUtils.register(context, ORE_CINNABAR, cinnabarOre, commonOrePlacement(15, HeightRangePlacement.triangle(VerticalAnchor.absolute(16), VerticalAnchor.absolute(64))));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }

    private static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_) {
        return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
    }
}
