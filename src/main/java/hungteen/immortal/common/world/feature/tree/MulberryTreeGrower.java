package hungteen.immortal.common.world.feature.tree;

import hungteen.immortal.common.world.feature.ImmortalConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 12:37
 **/
public class MulberryTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean p_222911_) {
        return ImmortalConfiguredFeatures.MULBERRY_TREE.getHolder().get();
    }

}
