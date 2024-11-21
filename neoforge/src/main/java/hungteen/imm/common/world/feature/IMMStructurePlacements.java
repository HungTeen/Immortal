package hungteen.imm.common.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/29 9:47
 **/
public interface IMMStructurePlacements {

    ResourceKey<PlacedFeature> LAB_CHERRY_CHECKED = IMMPlacements.create("lab_cherry_checked");

    static void register(BootstrapContext<PlacedFeature> context) {
        final HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        final Holder<ConfiguredFeature<?, ?>> cherry = features.getOrThrow(TreeFeatures.CHERRY);
        PlacementUtils.register(context, LAB_CHERRY_CHECKED, cherry, PlacementUtils.filteredByBlockSurvival(Blocks.CHERRY_SAPLING));
    }
}
