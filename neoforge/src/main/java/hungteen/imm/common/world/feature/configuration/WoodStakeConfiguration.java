package hungteen.imm.common.world.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-06-30 21:49
 **/
public record WoodStakeConfiguration(IntProvider height, BlockStateProvider logState) implements FeatureConfiguration {

    public static final Codec<WoodStakeConfiguration> CODEC = RecordCodecBuilder.<WoodStakeConfiguration>mapCodec(instance -> instance.group(
            IntProvider.codec(0, Integer.MAX_VALUE).fieldOf("height").forGetter(WoodStakeConfiguration::height),
            BlockStateProvider.CODEC.fieldOf("log_state").forGetter(WoodStakeConfiguration::logState)
    ).apply(instance, WoodStakeConfiguration::new)).codec();

}
