package hungteen.imm.common.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/14 14:32
 */
public interface IMMTreePlacements {

    ResourceKey<PlacedFeature> TALL_BIRCH_CHECKED = IMMPlacements.create("tall_birch_checked");
    ResourceKey<PlacedFeature> TALL_BIRCH_WITH_BEES_CHECKED = IMMPlacements.create("tall_birch_with_bees_checked");
    ResourceKey<PlacedFeature> TALL_DARK_OAK_CHECKED = IMMPlacements.create("tall_dark_oak_checked");

    /**
     * {@link net.minecraft.data.worldgen.placement.TreePlacements#bootstrap(BootstrapContext)}
     */
    static void register(BootstrapContext<PlacedFeature> context) {
        final HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        final Holder<ConfiguredFeature<?, ?>> tallBirch = features.getOrThrow(IMMTreeFeatures.TALL_BIRCH_TREE);
        final Holder<ConfiguredFeature<?, ?>> tallBirchWithBees = features.getOrThrow(IMMTreeFeatures.TALL_BIRCH_TREE_WITH_BEES);
        final Holder<ConfiguredFeature<?, ?>> tallDarkOak = features.getOrThrow(IMMTreeFeatures.TALL_DARK_OAK);
        PlacementUtils.register(context, TALL_BIRCH_CHECKED, tallBirch, PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING));
        PlacementUtils.register(context, TALL_BIRCH_WITH_BEES_CHECKED, tallBirchWithBees, PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING));
        PlacementUtils.register(context, TALL_DARK_OAK_CHECKED, tallDarkOak, PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING));
    }

}
