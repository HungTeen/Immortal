package hungteen.imm.common.world.levelgen.dimension;

import com.mojang.datafixers.util.Pair;
import hungteen.imm.common.world.levelgen.IMMBiomes;
import hungteen.imm.common.world.levelgen.IMMDimensionTypes;
import hungteen.imm.common.world.levelgen.IMMNoiseSettings;
import hungteen.imm.common.world.levelgen.IMMSurfaceRules;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.NoiseSettings;

import java.util.List;
import java.util.OptionalLong;
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
    private final ResourceKey<Biome>[][] DEFAULT_MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{
            {Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null},
            {null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA},
            {Biomes.SUNFLOWER_PLAINS, null, null, Biomes.OLD_GROWTH_BIRCH_FOREST, null},
            {null, null, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE},
            {null, null, null, null, null}
    };
    private static final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][]{
            {IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS},
            {IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS},
            {IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS},
            {IMMBiomes.SPIRITUAL_SAVANNA, IMMBiomes.SPIRITUAL_SAVANNA, IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS, IMMBiomes.SPIRITUAL_PLAINS},
            {IMMBiomes.SPIRITUAL_DESERT, IMMBiomes.SPIRITUAL_DESERT, IMMBiomes.SPIRITUAL_DESERT, IMMBiomes.SPIRITUAL_DESERT, IMMBiomes.SPIRITUAL_DESERT}
    };
    private static final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null}
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
        for (int i = 0; i < TEMPERATURES.length; ++i) {
            final Climate.Parameter temperature = TEMPERATURES[i];
            for (int j = 0; j < HUMIDITIES.length; ++j) {
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

    public static void initDimensionType(BootstapContext<DimensionType> context) {
        context.register(IMMDimensionTypes.EAST_WORLD, new DimensionType(
                OptionalLong.empty(),
                true,
                false,
                false,
                true,
                1.0D,
                true,
                false,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                0.0F,
                new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)
        ));
    }

    public static void initNoiseSettings(BootstapContext<NoiseGeneratorSettings> context) {
        context.register(IMMNoiseSettings.EAST_WORLD, new NoiseGeneratorSettings(
                NoiseSettings.create(-64, 384, 1, 2),
                Blocks.STONE.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                NoiseRouterData.overworld(
                        context.lookup(Registries.DENSITY_FUNCTION),
                        context.lookup(Registries.NOISE),
                        false,
                        false),
                IMMSurfaceRules.overworldLike(false, true),
                spawnTarget(),
                63,
                false,
                true,
                true,
                false
        ));
    }

}
