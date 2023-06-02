package hungteen.imm.common.world.levelgen.feature;

import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 12:38
 **/
public class IMMFeatures {

//    public static final RegistryObject<ConfiguredFeature<?, ?>> MULBERRY_TREE = CONFIGURED_FEATURES.register("mulberry_tree", () ->
//            new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
//                    BlockStateProvider.simple(ImmortalWoods.MULBERRY.getLog()),
//                    new StraightTrunkPlacer(5, 6, 3),
//                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.AZALEA_LEAVES.defaultBlockState(), 3).add(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), 1)),
//                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 4),
//                    new TwoLayersFeatureSize(1, 0, 2)
//            ).build())
//    );

    public static void register(BootstapContext<ConfiguredFeature<?, ?>> context) {
        IMMOreFeatures.register(context);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> create(String name){
        return FeatureUtils.createKey(Util.prefixName(name));
    }


}
