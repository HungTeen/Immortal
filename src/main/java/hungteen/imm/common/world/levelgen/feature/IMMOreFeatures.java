package hungteen.imm.common.world.levelgen.feature;

import hungteen.imm.common.block.IMMBlocks;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/2 9:25
 */
public class IMMOreFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CINNABAR = IMMFeatures.create("ore_cinnabar");

    public static void register(BootstapContext<ConfiguredFeature<?, ?>> context) {
        final RuleTest matchStone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        final List<OreConfiguration.TargetBlockState> cinnabarTargetList = List.of(OreConfiguration.target(matchStone, IMMBlocks.CINNABAR_ORE.get().defaultBlockState()));
        FeatureUtils.register(context, ORE_CINNABAR, Feature.ORE, new OreConfiguration(cinnabarTargetList, 9));
    }

}
