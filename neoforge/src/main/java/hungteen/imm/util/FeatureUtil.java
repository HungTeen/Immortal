package hungteen.imm.util;

import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

import java.util.List;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/5 20:53
 **/
public class FeatureUtil {

    /**
     * {@link FeatureUtils#simplePatchConfiguration(Feature, FeatureConfiguration, List, int)}.
     */
    public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F feature, FC config, int tries) {
        return FeatureUtils.simplePatchConfiguration(feature, config, List.of(), tries);
    }

}
