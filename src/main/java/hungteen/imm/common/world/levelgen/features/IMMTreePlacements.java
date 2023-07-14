package hungteen.imm.common.world.levelgen.features;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
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
public class IMMTreePlacements {

    public static final ResourceKey<PlacedFeature> TALL_DARK_OAK_CHECKED = IMMPlacements.create("tall_dark_oak_checked");

    public static void register(BootstapContext<PlacedFeature> context) {
        final HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        final Holder<ConfiguredFeature<?, ?>> tallDarkOak = features.getOrThrow(IMMTreeFeatures.TALL_DARK_OAK);
        PlacementUtils.register(context, TALL_DARK_OAK_CHECKED, tallDarkOak, PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING));
    }

}
