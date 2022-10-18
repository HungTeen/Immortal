package hungteen.immortal.world.dimension;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import hungteen.immortal.utils.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-17 10:55
 *
 * DimensionType format can See {@link DimensionType}
 **/
public class ImmortalDimensions {

    public static final ResourceKey<Level> SPIRITUAL_LAND_DIMENSION = ResourceKey.create(Registry.DIMENSION_REGISTRY, Util.prefix("spiritual_land"));

    public static final ResourceKey<LevelStem> SPIRITUAL_LAND = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, Util.prefix("spiritual_land"));

    public static final ResourceKey<DimensionType> SPIRITUAL_LAND_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, SPIRITUAL_LAND.getRegistryName());

    /* Preset */

    public static final MultiNoiseBiomeSource.Preset SPIRITUAL_LAND_PRESET = new MultiNoiseBiomeSource.Preset(
            Util.prefix("spiritual_land"),
            (biomes) -> {
                ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
                SpiritualLandDimension.addBiomes((resourceKeyPair) -> {
                    builder.add(resourceKeyPair.mapSecond(biomes::getOrCreateHolder));
                });
                return new Climate.ParameterList<>(builder.build());
            }
    );

    public static void register(){

    }

}
