package hungteen.immortal.world.dimension;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import hungteen.immortal.utils.Util;
import hungteen.immortal.world.biome.ImmortalBiomes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.OptionalLong;
import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-18 12:49
 *
 * Look at {@link OverworldBiomeBuilder}
 **/
public class SpiritualLandDimension {

    private static final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
    private static final Climate.Parameter MUSHROOM_FIELDS_CONTINENTALNESS = Climate.Parameter.span(-1.2F, -1.05F);
    private static final Climate.Parameter DEEP_OCEAN_CONTINENTALNESS = Climate.Parameter.span(-1.05F, -0.455F);
    private static final Climate.Parameter OCEAN_CONTINENTALNESS = Climate.Parameter.span(-0.455F, -0.19F);
    private static final Climate.Parameter COAST_CONTINENTALNESS = Climate.Parameter.span(-0.19F, -0.11F);
    private static final Climate.Parameter INLAND_CONTINENTALNESS = Climate.Parameter.span(-0.11F, 0.55F);
    private static final Climate.Parameter NEAR_INLAND_CONTINENTALNESS = Climate.Parameter.span(-0.11F, 0.03F);
    private static final Climate.Parameter MID_INLAND_CONTINENTALNESS = Climate.Parameter.span(0.03F, 0.3F);
    private static final Climate.Parameter FAR_INLAND_CONTINENTALNESS = Climate.Parameter.span(0.3F, 1.0F);
    private static final Climate.Parameter[] TEMPERATURES = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.45F),
            Climate.Parameter.span(-0.45F, -0.15F),
            Climate.Parameter.span(-0.15F, 0.2F),
            Climate.Parameter.span(0.2F, 0.55F),
            Climate.Parameter.span(0.55F, 1.0F)
    };
    private static final Climate.Parameter[] HUMIDITIES = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.35F),
            Climate.Parameter.span(-0.35F, -0.1F),
            Climate.Parameter.span(-0.1F, 0.1F),
            Climate.Parameter.span(0.1F, 0.3F),
            Climate.Parameter.span(0.3F, 1.0F)
    };
    private static final Climate.Parameter[] EROSIONS = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.78F),
            Climate.Parameter.span(-0.78F, -0.375F),
            Climate.Parameter.span(-0.375F, -0.2225F),
            Climate.Parameter.span(-0.2225F, 0.05F),
            Climate.Parameter.span(0.05F, 0.45F),
            Climate.Parameter.span(0.45F, 0.55F),
            Climate.Parameter.span(0.55F, 1.0F)
    };
    /* Middle Biome Table With Temperature And Humidity. */
    private static final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][]{
            {ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS},
            {ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS},
            {ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS},
            {ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS},
            {ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS}
    };
    private static final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{
            {ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS},
            {ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS},
            {ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS},
            {ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS},
            {ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS, ImmortalBiomes.SPIRITUAL_PLAINS}
    };

    protected static void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
        addOffCoastBiomes(consumer);
        addInlandBiomes(consumer);
        addUndergroundBiomes(consumer);
    }

    private static void addOffCoastBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187196_) {
//        this.addSurfaceBiome(p_187196_, this.FULL_RANGE, this.FULL_RANGE, this.mushroomFieldsContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.MUSHROOM_FIELDS);
//
//        for(int i = 0; i < this.temperatures.length; ++i) {
//            Climate.Parameter climate$parameter = this.temperatures[i];
//            this.addSurfaceBiome(p_187196_, climate$parameter, this.FULL_RANGE, this.deepOceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS[0][i]);
//            this.addSurfaceBiome(p_187196_, climate$parameter, this.FULL_RANGE, this.oceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS[1][i]);
//        }
    }

    private static void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
        addValleys(consumer, Climate.Parameter.span(-0.05F, 0.05F));
//        this.addMidSlice(consumer, Climate.Parameter.span(-1.0F, -0.93333334F));
//        this.addMidSlice(consumer, Climate.Parameter.span(-0.4F, -0.26666668F));
//        this.addMidSlice(consumer, Climate.Parameter.span(0.26666668F, 0.4F));
//        this.addMidSlice(consumer, Climate.Parameter.span(0.93333334F, 1.0F));
//
//        this.addHighSlice(consumer, Climate.Parameter.span(-0.93333334F, -0.7666667F));
//        this.addHighSlice(consumer, Climate.Parameter.span(-0.56666666F, -0.4F));
//        this.addHighSlice(consumer, Climate.Parameter.span(0.4F, 0.56666666F));
//        this.addHighSlice(consumer, Climate.Parameter.span(0.7666667F, 0.93333334F));



//        this.addLowSlice(consumer, Climate.Parameter.span(-0.26666668F, -0.05F));
//        this.addLowSlice(consumer, Climate.Parameter.span(0.05F, 0.26666668F));
//
//        this.addPeaks(consumer, Climate.Parameter.span(-0.7666667F, -0.56666666F));
//        this.addPeaks(consumer, Climate.Parameter.span(0.56666666F, 0.7666667F));

    }

    private static void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
        /* Add Valley Rivers */
        //       addSurfaceBiome(consumer, MIDDLE_BIOMES, FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.FROZEN_RIVER);
        /* Add Middle Biomes */
        for(int i = 0; i < TEMPERATURES.length; ++i) {
            final Climate.Parameter temperature = TEMPERATURES[i];
            for(int j = 0; j < HUMIDITIES.length; ++j) {
                final Climate.Parameter humidity = HUMIDITIES[j];
                final ResourceKey<Biome> biomeKey = pickMiddleBiome(i, j, weirdness);
                addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(MID_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS), Climate.Parameter.span(EROSIONS[0], EROSIONS[1]), weirdness, 0.0F, biomeKey);
            }
        }
    }

    private static void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biomeKey) {
        consumer.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(0.0F), weirdness, offset), biomeKey));
        consumer.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1.0F), weirdness, offset), biomeKey));
    }

    private static void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
//        addUndergroundBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(0.8F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.DRIPSTONE_CAVES);
//        addUndergroundBiome(consumer, this.FULL_RANGE, Climate.Parameter.span(0.7F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.LUSH_CAVES);
    }

    private static ResourceKey<Biome> pickMiddleBiome(int temperatureId, int humidityId, Climate.Parameter weirdness) {
        if (weirdness.max() < 0L) {
            return MIDDLE_BIOMES[temperatureId][humidityId];
        } else {
            ResourceKey<Biome> biomeKey = MIDDLE_BIOMES_VARIANT[temperatureId][humidityId];
            return biomeKey == null ? MIDDLE_BIOMES[temperatureId][humidityId] : biomeKey;
        }
    }

    // TODO 修改type。
    public static DimensionType getDimensionType() {
        return DimensionType.create(
                OptionalLong.empty(),
                true,
                false,
                false,
                true,
                1.0D,
                false,
                false,
                true,
                false,
                true,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                DimensionType.OVERWORLD_EFFECTS,
                0.0F
        );
    }

}
