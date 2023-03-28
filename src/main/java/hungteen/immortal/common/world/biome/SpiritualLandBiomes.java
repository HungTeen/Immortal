package hungteen.immortal.common.world.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-18 14:11
 *
 * Look at {@link net.minecraft.data.worldgen.biome.OverworldBiomes}
 **/
public class SpiritualLandBiomes {

//    public static Biome plains(HolderGetter<PlacedFeature> p_256382_, HolderGetter<ConfiguredWorldCarver<?>> p_256173) {
//        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
//        BiomeGenerationSettings.Builder generationBuilder = new BiomeGenerationSettings.Builder(p_256382_, p_256173);
//        globalGeneration(generationBuilder);
//        BiomeDefaultFeatures.plainsSpawns(spawnBuilder);
//        BiomeDefaultFeatures.addPlainGrass(generationBuilder);
//        BiomeDefaultFeatures.addPlainVegetation(generationBuilder);
//        BiomeDefaultFeatures.addDefaultOres(generationBuilder);
//        BiomeDefaultFeatures.addDefaultSoftDisks(generationBuilder);
//        BiomeDefaultFeatures.addDefaultMushrooms(generationBuilder);
//        return biome(Biome.Precipitation.RAIN, 0.8F, 0.4F, spawnBuilder, generationBuilder, null);
//    }
//
//    public static Biome savanna() {
//        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
//        BiomeGenerationSettings.Builder generationBuilder = new BiomeGenerationSettings.Builder();
//        globalGeneration(generationBuilder);
//        BiomeDefaultFeatures.plainsSpawns(spawnBuilder);
//        BiomeDefaultFeatures.addPlainGrass(generationBuilder);
//        BiomeDefaultFeatures.addPlainVegetation(generationBuilder);
//        BiomeDefaultFeatures.addDefaultOres(generationBuilder);
//        BiomeDefaultFeatures.addDefaultSoftDisks(generationBuilder);
//        BiomeDefaultFeatures.addDefaultMushrooms(generationBuilder);
//        return biome(Biome.Precipitation.NONE, 2.0F, 0.0F, spawnBuilder, generationBuilder, null);
//    }
//
//    public static Biome desert() {
//        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
//        BiomeGenerationSettings.Builder generationBuilder = new BiomeGenerationSettings.Builder();
//        globalGeneration(generationBuilder);
//        BiomeDefaultFeatures.plainsSpawns(spawnBuilder);
//        BiomeDefaultFeatures.addPlainGrass(generationBuilder);
//        BiomeDefaultFeatures.addPlainVegetation(generationBuilder);
//        BiomeDefaultFeatures.addDefaultOres(generationBuilder);
//        BiomeDefaultFeatures.addDefaultSoftDisks(generationBuilder);
//        BiomeDefaultFeatures.addDefaultMushrooms(generationBuilder);
//        return biome(Biome.Precipitation.NONE, 2.0F, 0.0F, spawnBuilder, generationBuilder, null);
//    }

    /**
     * Generations that every biome will have.
     */
    private static void globalGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    private static Biome biome(Biome.Precipitation precipitation, float temperature, float downfall, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder generationBuilder, @Nullable Music music) {
        return biome(precipitation, temperature, downfall, 4159204, 329011, spawnBuilder, generationBuilder, music);
    }

    private static Biome biome(Biome.Precipitation precipitation, float temperature, float downfall, int waterColor, int waterFogColor, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder generationBuilder, @Nullable Music music) {
        return new Biome.BiomeBuilder()
                .precipitation(precipitation)
                .temperature(temperature)
                .downfall(downfall)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(waterColor)
                        .waterFogColor(waterFogColor)
                        .fogColor(12638463)
                        .skyColor(calculateSkyColor(temperature))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(music).build()
                )
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(generationBuilder.build())
                .build();
    }

    protected static int calculateSkyColor(float p_194844_) {
        float $$1 = p_194844_ / 3.0F;
        $$1 = Mth.clamp($$1, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
    }

}
