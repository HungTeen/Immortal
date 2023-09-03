package hungteen.imm.common.world.levelgen.dimension;

import com.mojang.datafixers.util.Pair;
import hungteen.imm.common.world.levelgen.IMMBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;

import java.util.List;
import java.util.function.Consumer;

/**
 * Look at {@link OverworldBiomeBuilder}
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-18 12:49
 **/
public class EastWorldDimension {

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
    /**
     * Middle Biome Table With Temperature And Humidity.
     */
    private final ResourceKey<Biome>[][] DEFAULT_MIDDLE_BIOMES = new ResourceKey[][]{
            {Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.TAIGA},
            {Biomes.PLAINS, Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA},
            {Biomes.FLOWER_FOREST, Biomes.PLAINS, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST},
            {Biomes.SAVANNA, Biomes.SAVANNA, Biomes.FOREST, Biomes.JUNGLE, Biomes.JUNGLE},
            {Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT}
    };
    private static final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][]{
            {IMMBiomes.PLAINS, IMMBiomes.PLAINS, IMMBiomes.PLAINS, IMMBiomes.PLAINS, IMMBiomes.PLAINS},
            {IMMBiomes.PLAINS, IMMBiomes.PLAINS, IMMBiomes.PLAINS, IMMBiomes.PLAINS, IMMBiomes.PLAINS},
            {IMMBiomes.PLAINS, IMMBiomes.PLAINS, IMMBiomes.BIRCH_FOREST, IMMBiomes.BIRCH_FOREST, IMMBiomes.PLAINS},
            {IMMBiomes.SAVANNA, IMMBiomes.SAVANNA, IMMBiomes.BIRCH_FOREST, IMMBiomes.BAMBOO_JUNGLE, IMMBiomes.BAMBOO_JUNGLE},
            {IMMBiomes.DESERT, IMMBiomes.DESERT, IMMBiomes.DESERT, IMMBiomes.DESERT, IMMBiomes.DESERT}
    };
    private final ResourceKey<Biome>[][] DEFAULT_MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{
            {Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null},
            {null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA},
            {Biomes.SUNFLOWER_PLAINS, null, null, Biomes.OLD_GROWTH_BIRCH_FOREST, null},
            {null, null, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE},
            {null, null, null, null, null}
    };
    private static final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, IMMBiomes.CUT_BIRCH_FOREST, IMMBiomes.CUT_BIRCH_FOREST, null},
            {null, null, IMMBiomes.CUT_BIRCH_FOREST, null, null},
            {null, null, null, null, null}
    };
    private static final ResourceKey<Biome>[][] DEFAULT_PLATEAU_BIOMES = new ResourceKey[][]{
            {Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA},
            {Biomes.MEADOW, Biomes.MEADOW, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA},
            {Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.DARK_FOREST},
            {Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA_PLATEAU, Biomes.FOREST, Biomes.FOREST, Biomes.JUNGLE},
            {Biomes.BADLANDS, Biomes.BADLANDS, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.WOODED_BADLANDS}
    };
    private static final ResourceKey<Biome>[][] PLATEAU_BIOMES = new ResourceKey[][]{
            {IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW},
            {IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW},
            {IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW},
            {IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW},
            {IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW, IMMBiomes.MEADOW}
    };
    private static final ResourceKey<Biome>[][] DEFAULT_PLATEAU_BIOMES_VARIANT = new ResourceKey[][]{
            {Biomes.ICE_SPIKES, null, null, null, null},
            {null, null, Biomes.MEADOW, Biomes.MEADOW, Biomes.OLD_GROWTH_PINE_TAIGA},
            {null, null, Biomes.FOREST, Biomes.BIRCH_FOREST, null},
            {null, null, null, null, null},
            {Biomes.ERODED_BADLANDS, Biomes.ERODED_BADLANDS, null, null, null}
    };
    private static final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT = new ResourceKey[][]{
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null}
    };




    public static void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
        addOffCoastBiomes(consumer);
        addInlandBiomes(consumer);
        addUndergroundBiomes(consumer);
    }

    private static void addOffCoastBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187196_) {
//        addSurfaceBiome(p_187196_, FULL_RANGE, FULL_RANGE, mushroomFieldsContinentalness, FULL_RANGE, FULL_RANGE, 0.0F, Biomes.MUSHROOM_FIELDS);
//
//        for(int i = 0; i < temperatures.length; ++i) {
//            Climate.Parameter climate$parameter = temperatures[i];
//            addSurfaceBiome(p_187196_, climate$parameter, FULL_RANGE, deepOceanContinentalness, FULL_RANGE, FULL_RANGE, 0.0F, OCEANS[0][i]);
//            addSurfaceBiome(p_187196_, climate$parameter, FULL_RANGE, oceanContinentalness, FULL_RANGE, FULL_RANGE, 0.0F, OCEANS[1][i]);
//        }
    }

    private static void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
        addValleys(consumer, Climate.Parameter.span(-0.05F, 0.05F));

//        addLowSlice(consumer, Climate.Parameter.span(-0.26666668F, -0.05F));
//        addLowSlice(consumer, Climate.Parameter.span(0.05F, 0.26666668F));

        addMidSlice(consumer, Climate.Parameter.span(-1.0F, -0.93333334F));
        addMidSlice(consumer, Climate.Parameter.span(-0.4F, -0.26666668F));
        addMidSlice(consumer, Climate.Parameter.span(0.26666668F, 0.4F));
        addMidSlice(consumer, Climate.Parameter.span(0.93333334F, 1.0F));

//        addHighSlice(consumer, Climate.Parameter.span(-0.93333334F, -0.7666667F));
//        addHighSlice(consumer, Climate.Parameter.span(-0.56666666F, -0.4F));
//        addHighSlice(consumer, Climate.Parameter.span(0.4F, 0.56666666F));
//        addHighSlice(consumer, Climate.Parameter.span(0.7666667F, 0.93333334F));

//
//        addPeaks(consumer, Climate.Parameter.span(-0.7666667F, -0.56666666F));
//        addPeaks(consumer, Climate.Parameter.span(0.56666666F, 0.7666667F));

    }

    private static void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
        /* Add Valley Rivers */
        //       addSurfaceBiome(consumer, MIDDLE_BIOMES, FULL_RANGE, Climate.Parameter.span(inlandContinentalness, farInlandContinentalness), EROSIONS[6], weirdness, 0.0F, Biomes.FROZEN_RIVER);
        /* Add Middle Biomes */
        for (int i = 0; i < TEMPERATURES.length; ++i) {
            final Climate.Parameter temperature = TEMPERATURES[i];
            for (int j = 0; j < HUMIDITIES.length; ++j) {
                final Climate.Parameter humidity = HUMIDITIES[j];
                final ResourceKey<Biome> biomeKey = pickMiddleBiome(i, j, weirdness);
                addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(MID_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS), Climate.Parameter.span(EROSIONS[0], EROSIONS[1]), weirdness, 0.0F, biomeKey);
            }
        }
    }

    private static void addLowSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
//        addSurfaceBiome(consumer, FULL_RANGE, FULL_RANGE, COAST_CONTINENTALNESS, Climate.Parameter.span(EROSIONS[0], EROSIONS[2]), weirdness, 0.0F, Biomes.STONY_SHORE);
//        addSurfaceBiome(consumer, temperature(1, 2), FULL_RANGE, nearToFar(), EROSIONS[6], weirdness, 0.0F, Biomes.SWAMP);
//        addSurfaceBiome(consumer, temperature(3, 4), FULL_RANGE, nearToFar(), EROSIONS[6], weirdness, 0.0F, Biomes.MANGROVE_SWAMP);

        for(int i = 0; i < TEMPERATURES.length; ++i) {
            final Climate.Parameter temperature = TEMPERATURES[i];
            for(int j = 0; j < HUMIDITIES.length; ++j) {
                final Climate.Parameter humidity = HUMIDITIES[j];
                ResourceKey<Biome> resourcekey = pickMiddleBiome(i, j, weirdness);
//                ResourceKey<Biome> resourcekey1 = pickMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
//                ResourceKey<Biome> resourcekey2 = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
//                ResourceKey<Biome> resourcekey3 = pickBeachBiome(i, j);
//                ResourceKey<Biome> resourcekey4 = maybePickWindsweptSavannaBiome(i, j, weirdness, resourcekey);
//                ResourceKey<Biome> resourcekey5 = pickShatteredCoastBiome(i, j, weirdness);
//                addSurfaceBiome(consumer, temperature, humidity, nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, resourcekey1);
//                addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, resourcekey2);
//                addSurfaceBiome(consumer, temperature, humidity, nearInlandContinentalness, Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0F, resourcekey);
//                addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0F, resourcekey1);
//                addSurfaceBiome(consumer, temperature, humidity, coastContinentalness, Climate.Parameter.span(erosions[3], erosions[4]), weirdness, 0.0F, resourcekey3);
                addSurfaceBiome(consumer, temperature, humidity, nearToFar(), erosion(4), weirdness, 0.0F, resourcekey);
//                addSurfaceBiome(consumer, temperature, humidity, coastContinentalness, erosions[5], weirdness, 0.0F, resourcekey5);
//                addSurfaceBiome(consumer, temperature, humidity, nearInlandContinentalness, erosions[5], weirdness, 0.0F, resourcekey4);
                addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(MID_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS), erosion(5), weirdness, 0.0F, resourcekey);
//                addSurfaceBiome(consumer, temperature, humidity, coastContinentalness, erosions[6], weirdness, 0.0F, resourcekey3);
                if (i == 0) {
                    addSurfaceBiome(consumer, temperature, humidity, nearToFar(), erosion(6), weirdness, 0.0F, resourcekey);
                }
            }
        }

    }

    private static void addMidSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
//        addSurfaceBiome(consumer, FULL_RANGE, FULL_RANGE, coastContinentalness, Climate.Parameter.span(EROSIONS[0], EROSIONS[2]), weirdness, 0.0F, Biomes.STONY_SHORE);
//        addSurfaceBiome(consumer, Climate.Parameter.span(temperatures[1], temperatures[2]), FULL_RANGE, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), EROSIONS[6], weirdness, 0.0F, Biomes.SWAMP);
//        addSurfaceBiome(consumer, Climate.Parameter.span(temperatures[3], temperatures[4]), FULL_RANGE, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), EROSIONS[6], weirdness, 0.0F, Biomes.MANGROVE_SWAMP);

        for(int i = 0; i < TEMPERATURES.length; ++i) {
            final Climate.Parameter temperature = TEMPERATURES[i];

            for(int j = 0; j < HUMIDITIES.length; ++j) {
                final Climate.Parameter humidity = HUMIDITIES[j];
                final ResourceKey<Biome> resourcekey = pickMiddleBiome(i, j, weirdness);
//                ResourceKey<Biome> resourcekey1 = pickMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
//                ResourceKey<Biome> resourcekey2 = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
//                ResourceKey<Biome> resourcekey3 = pickShatteredBiome(i, j, weirdness);
                final ResourceKey<Biome> resourcekey4 = pickPlateauBiome(i, j, weirdness);
//                ResourceKey<Biome> resourcekey5 = pickBeachBiome(i, j);
//                ResourceKey<Biome> resourcekey6 = maybePickWindsweptSavannaBiome(i, j, weirdness, resourcekey);
//                ResourceKey<Biome> resourcekey7 = pickShatteredCoastBiome(i, j, weirdness);
//                ResourceKey<Biome> resourcekey8 = pickSlopeBiome(i, j, weirdness);
//                addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(NEAR_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS), EROSIONS[0], weirdness, 0.0F, resourcekey8);
//                addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(NEAR_INLAND_CONTINENTALNESS, MID_INLAND_CONTINENTALNESS), EROSIONS[1], weirdness, 0.0F, resourcekey2);
//                addSurfaceBiome(consumer, temperature, humidity, FAR_INLAND_CONTINENTALNESS, EROSIONS[1], weirdness, 0.0F, i == 0 ? resourcekey8 : resourcekey4);
                addSurfaceBiome(consumer, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, erosion(2), weirdness, 0.0F, resourcekey);
//                addSurfaceBiome(consumer, temperature, humidity, MID_INLAND_CONTINENTALNESS, EROSIONS[2], weirdness, 0.0F, resourcekey1);
                addSurfaceBiome(consumer, temperature, humidity, FAR_INLAND_CONTINENTALNESS, erosion(2), weirdness, 0.0F, resourcekey4);
                addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(COAST_CONTINENTALNESS, NEAR_INLAND_CONTINENTALNESS), erosion(3), weirdness, 0.0F, resourcekey);
//                addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(MID_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS), EROSIONS[3], weirdness, 0.0F, resourcekey1);
                if (weirdness.max() < 0L) {
//                    addSurfaceBiome(consumer, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[4], weirdness, 0.0F, resourcekey5);
                    addSurfaceBiome(consumer, temperature, humidity, nearToFar(), erosion(4), weirdness, 0.0F, resourcekey);
                } else {
                    addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(COAST_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS), erosion(4), weirdness, 0.0F, resourcekey);
                }

//                addSurfaceBiome(consumer, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[5], weirdness, 0.0F, resourcekey7);
//                addSurfaceBiome(consumer, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[5], weirdness, 0.0F, resourcekey6);
//                addSurfaceBiome(consumer, temperature, humidity, Climate.Parameter.span(MID_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS), EROSIONS[5], weirdness, 0.0F, resourcekey3);
                if (weirdness.max() < 0L) {
//                    addSurfaceBiome(consumer, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[6], weirdness, 0.0F, resourcekey5);
                } else {
                    addSurfaceBiome(consumer, temperature, humidity, COAST_CONTINENTALNESS, erosion(6), weirdness, 0.0F, resourcekey);
                }

                if (i == 0) {
                    addSurfaceBiome(consumer, temperature, humidity, nearToFar(), erosion(6), weirdness, 0.0F, resourcekey);
                }
            }
        }

    }

    private static void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biomeKey) {
        consumer.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(0.0F), weirdness, offset), biomeKey));
        consumer.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1.0F), weirdness, offset), biomeKey));
    }

    private static void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
//        addUndergroundBiome(consumer, FULL_RANGE, FULL_RANGE, Climate.Parameter.span(0.8F, 1.0F), FULL_RANGE, FULL_RANGE, 0.0F, Biomes.DRIPSTONE_CAVES);
//        addUndergroundBiome(consumer, FULL_RANGE, Climate.Parameter.span(0.7F, 1.0F), FULL_RANGE, FULL_RANGE, FULL_RANGE, 0.0F, Biomes.LUSH_CAVES);
    }

    private static ResourceKey<Biome> pickMiddleBiome(int temperatureId, int humidityId, Climate.Parameter weirdness) {
        if (weirdness.max() < 0L) {
            return MIDDLE_BIOMES[temperatureId][humidityId];
        } else {
            ResourceKey<Biome> biomeKey = MIDDLE_BIOMES_VARIANT[temperatureId][humidityId];
            return biomeKey == null ? MIDDLE_BIOMES[temperatureId][humidityId] : biomeKey;
        }
    }

    private static ResourceKey<Biome> pickPlateauBiome(int temperatureId, int humidityId, Climate.Parameter weirdness) {
        ResourceKey<Biome> resourcekey = PLATEAU_BIOMES_VARIANT[temperatureId][humidityId];
        return weirdness.max() >= 0L && resourcekey != null ? resourcekey : PLATEAU_BIOMES[temperatureId][humidityId];
    }

    private static Climate.Parameter nearToFar(){
        return Climate.Parameter.span(NEAR_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS);
    }

    private static Climate.Parameter erosion(int from, int to){
        return Climate.Parameter.span(EROSIONS[from], EROSIONS[to]);
    }

    private static Climate.Parameter erosion(int pos){
        return EROSIONS[pos];
    }

    private static Climate.Parameter temperature(int from, int to){
        return Climate.Parameter.span(TEMPERATURES[from], TEMPERATURES[to]);
    }

    private static Climate.Parameter temperature(int pos){
        return TEMPERATURES[pos];
    }

    public static List<Climate.ParameterPoint> spawnTarget() {
        Climate.Parameter zero = Climate.Parameter.point(0.0F);
        return List.of(new Climate.ParameterPoint(
                        FULL_RANGE,
                        FULL_RANGE,
                        Climate.Parameter.span(INLAND_CONTINENTALNESS, FULL_RANGE),
                        FULL_RANGE,
                        zero,
                        Climate.Parameter.span(-1.0F, -0.16F),
                        0L
                ), new Climate.ParameterPoint(
                        FULL_RANGE,
                        FULL_RANGE,
                        Climate.Parameter.span(INLAND_CONTINENTALNESS, FULL_RANGE),
                        FULL_RANGE,
                        zero,
                        Climate.Parameter.span(0.16F, 1.0F),
                        0L
                )
        );
    }

}
