package hungteen.immortal.data.codec;

import hungteen.htlib.data.HTCodecGen;
import hungteen.immortal.utils.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-10 13:45
 **/
public class DimensionTypeGen extends HTCodecGen {

    public DimensionTypeGen(DataGenerator generator) {
        super(generator, Util.id());
    }

    @Override
    public void run(CachedOutput cache) {
//        WritableRegistry<DimensionType> registry = new MappedRegistry<>(Registry.DIMENSION_TYPE_REGISTRY, Lifecycle.experimental(), null);
//
//        registry.register(ImmortalDimensions.SPIRITUAL_LAND_TYPE.getKey(), SpiritualLandDimension.getDimensionType(), Lifecycle.stable());
//
//        register(cache, Registry.DIMENSION_TYPE_REGISTRY, registry, DimensionType.DIRECT_CODEC);
    }

    @Override
    public String getName() {
        return this.modId + " dimension types";
    }
}
