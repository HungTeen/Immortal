package hungteen.imm.common.world.levelgen.features;

import hungteen.imm.common.world.levelgen.features.configuration.HorizontalStakeConfiguration;
import hungteen.imm.common.world.levelgen.features.configuration.WoodStakeConfiguration;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-29 22:36
 **/
public class IMMPlantFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_STAKE = IMMFeatures.create("oak_stake");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_HORIZONTAL_STAKE = IMMFeatures.create("oak_horizontal_stake");

    public static void register(BootstapContext<ConfiguredFeature<?, ?>> context) {
        FeatureUtils.register(context, OAK_STAKE, IMMFeatures.WOOD_STAKE.get(), new WoodStakeConfiguration(
                UniformInt.of(1, 3), SimpleStateProvider.simple(Blocks.OAK_LOG.defaultBlockState())
        ));
        FeatureUtils.register(context, OAK_HORIZONTAL_STAKE, IMMFeatures.HORIZONTAL_STAKE.get(), new HorizontalStakeConfiguration(
                UniformInt.of(3, 9), UniformInt.of(1, 5),
                SimpleStateProvider.simple(Blocks.OAK_LOG.defaultBlockState())
        ));
    }

}
