package hungteen.imm.common.world.levelgen.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-30 22:55
 **/
public record HorizontalStakeConfiguration(IntProvider length, IntProvider width, BlockStateProvider logState) implements FeatureConfiguration {

    public static final Codec<HorizontalStakeConfiguration> CODEC = RecordCodecBuilder.<HorizontalStakeConfiguration>mapCodec(instance -> instance.group(
            IntProvider.codec(0, Integer.MAX_VALUE).fieldOf("length").forGetter(HorizontalStakeConfiguration::length),
            IntProvider.codec(0, Integer.MAX_VALUE).fieldOf("width").forGetter(HorizontalStakeConfiguration::width),
            BlockStateProvider.CODEC.fieldOf("log_state").forGetter(HorizontalStakeConfiguration::logState)
    ).apply(instance, HorizontalStakeConfiguration::new)).codec();

}
