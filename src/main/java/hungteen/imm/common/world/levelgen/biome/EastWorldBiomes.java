package hungteen.imm.common.world.levelgen.biome;

import hungteen.imm.common.world.levelgen.IMMBiomes;
import hungteen.imm.common.world.levelgen.features.IMMPlantPlacements;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.Nullable;

/**
 * Look at {@link net.minecraft.data.worldgen.biome.OverworldBiomes}.
 *
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-18 14:11
 **/
public class EastWorldBiomes {

    public static void initBiomes(BootstapContext<Biome> context) {
        final HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        final HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);
        context.register(IMMBiomes.PLAINS, plains(features, carvers, false, false));
        context.register(IMMBiomes.SAVANNA, savanna(features, carvers, false, false));
        context.register(IMMBiomes.DESERT, desert(features, carvers));
        context.register(IMMBiomes.BAMBOO_JUNGLE, bambooJungle(features, carvers));
        context.register(IMMBiomes.MEADOW, meadow(features, carvers));
        context.register(IMMBiomes.BIRCH_FOREST, forest(features, carvers, false, false));
        context.register(IMMBiomes.CUT_BIRCH_FOREST, forest(features, carvers, true, true));
    }

    /**
     * East World Plain: <br>
     */
    private static Biome plains(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean sunflower, boolean snowy) {
        final MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        if (snowy) {
            spawnBuilder.creatureGenerationProbability(0.07F);
            BiomeDefaultFeatures.snowySpawns(spawnBuilder);
        } else {
            BiomeDefaultFeatures.plainsSpawns(spawnBuilder);
        }

        final BiomeGenerationSettings.Builder genBuilder = new BiomeGenerationSettings.Builder(features, carvers);
        EastWorldFeatures.globalGeneration(genBuilder);
        EastWorldFeatures.addOres(genBuilder, true, false);
        BiomeDefaultFeatures.addDefaultSoftDisks(genBuilder);
        if (snowy) {
            BiomeDefaultFeatures.addSnowyTrees(genBuilder);
            BiomeDefaultFeatures.addDefaultFlowers(genBuilder);
            BiomeDefaultFeatures.addDefaultGrass(genBuilder);
//            if (p_194884_) {
//                generationBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.ICE_SPIKE);
//                generationBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.ICE_PATCH);
//            }
        } else {
            BiomeDefaultFeatures.addPlainGrass(genBuilder);
            BiomeDefaultFeatures.addPlainVegetation(genBuilder);
            if (sunflower) {
                genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUNFLOWER);
            }
        }
        BiomeDefaultFeatures.addDefaultMushrooms(genBuilder);
        if (sunflower) {
            genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE);
            genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
        } else {
            BiomeDefaultFeatures.addDefaultExtraVegetation(genBuilder);
        }

        return biome(true, snowy ? 0F : 0.8F, snowy ? 0.5F : 0.4F, spawnBuilder, genBuilder, null);
    }

    public static Biome meadow(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        final BiomeGenerationSettings.Builder genBuilder = new BiomeGenerationSettings.Builder(features, carvers);
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
//        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 1, 2)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 2, 6)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 2, 2, 4));
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        EastWorldFeatures.globalGeneration(genBuilder);
        EastWorldFeatures.addOres(genBuilder, false, false);
        BiomeDefaultFeatures.addDefaultSoftDisks(genBuilder);
        BiomeDefaultFeatures.addPlainGrass(genBuilder);
        BiomeDefaultFeatures.addMeadowVegetation(genBuilder);

        BiomeDefaultFeatures.addExtraEmeralds(genBuilder);
        BiomeDefaultFeatures.addInfestedStone(genBuilder);
        Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_MEADOW);
        return biome(true, 0.5F, 0.8F, 937679, 329011, null, null, spawnBuilder, genBuilder, music);
    }

    /**
     * Spawn Ravager.
     */
    private static Biome savanna(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean windSwept, boolean plateau) {
        final MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.RAVAGER, 1, 1, 1));
        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
//        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 1, 2, 6)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 1, 1));
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);
//        if (plateau) {
//            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.LLAMA, 8, 4, 4));
//        }

        final BiomeGenerationSettings.Builder genBuilder = new BiomeGenerationSettings.Builder(features, carvers);
        EastWorldFeatures.globalGeneration(genBuilder);
        EastWorldFeatures.addOres(genBuilder, false, false);
        BiomeDefaultFeatures.addDefaultSoftDisks(genBuilder);
        if (windSwept) {
            BiomeDefaultFeatures.addShatteredSavannaTrees(genBuilder);
            BiomeDefaultFeatures.addDefaultFlowers(genBuilder);
            BiomeDefaultFeatures.addShatteredSavannaGrass(genBuilder);
        } else {
            BiomeDefaultFeatures.addSavannaTrees(genBuilder);
            BiomeDefaultFeatures.addWarmFlowers(genBuilder);
            BiomeDefaultFeatures.addSavannaExtraGrass(genBuilder);
            BiomeDefaultFeatures.addSavannaGrass(genBuilder);
        }
        BiomeDefaultFeatures.addDefaultMushrooms(genBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(genBuilder);

        return biome(false, 2.0F, 0.0F, spawnBuilder, genBuilder, null);
    }

    /**
     *
     */
    private static Biome desert(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        final MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3));

        final BiomeGenerationSettings.Builder genBuilder = new BiomeGenerationSettings.Builder(features, carvers);
        EastWorldFeatures.globalGeneration(genBuilder);
        EastWorldFeatures.addOres(genBuilder, false, false);
        BiomeDefaultFeatures.addDefaultSoftDisks(genBuilder);
        BiomeDefaultFeatures.addFossilDecoration(genBuilder);
        BiomeDefaultFeatures.addDefaultFlowers(genBuilder);
        BiomeDefaultFeatures.addDefaultGrass(genBuilder);
        BiomeDefaultFeatures.addDesertVegetation(genBuilder);
        BiomeDefaultFeatures.addDefaultMushrooms(genBuilder);
        BiomeDefaultFeatures.addDesertExtraVegetation(genBuilder);
        BiomeDefaultFeatures.addDesertExtraDecoration(genBuilder);

        return biome(false, 2.0F, 0.0F, spawnBuilder, genBuilder, null);
    }

    public static Biome forest(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean cut, boolean hasFlower) {
        final MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);
        if (hasFlower) {
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3));
        } else {
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 5, 4, 4));
        }

        final BiomeGenerationSettings.Builder genBuilder = new BiomeGenerationSettings.Builder(features, carvers);
        EastWorldFeatures.globalGeneration(genBuilder);
        if (hasFlower) {
            genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FOREST_FLOWERS);
        } else {
            BiomeDefaultFeatures.addForestFlowers(genBuilder);
        }
        BiomeDefaultFeatures.addDefaultOres(genBuilder);
        BiomeDefaultFeatures.addDefaultSoftDisks(genBuilder);
        if (hasFlower) {
            genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_FLOWER_FOREST);
            genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FLOWER_FOREST);
            BiomeDefaultFeatures.addDefaultGrass(genBuilder);
        } else {
            BiomeDefaultFeatures.addBirchTrees(genBuilder);
            BiomeDefaultFeatures.addDefaultFlowers(genBuilder);
            BiomeDefaultFeatures.addForestGrass(genBuilder);
        }
        BiomeDefaultFeatures.addDefaultMushrooms(genBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(genBuilder);
        if (cut) {
            genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, IMMPlantPlacements.OAK_STAKE);
            genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, IMMPlantPlacements.OAK_HORIZONTAL_STAKE);
        }

        final Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_JUNGLE_AND_FOREST);
        return biome(true, 0.6F, 0.6F, spawnBuilder, genBuilder, music);
    }

    private static Biome bambooJungle(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.baseJungleSpawns(spawnBuilder);

        BiomeGenerationSettings.Builder genBuilder = new BiomeGenerationSettings.Builder(features, carvers);
        EastWorldFeatures.globalGeneration(genBuilder);
        EastWorldFeatures.addOres(genBuilder, true, false);
        BiomeDefaultFeatures.addDefaultSoftDisks(genBuilder);
        BiomeDefaultFeatures.addBambooVegetation(genBuilder);

        BiomeDefaultFeatures.addPlainGrass(genBuilder);
        BiomeDefaultFeatures.addPlainVegetation(genBuilder);
        BiomeDefaultFeatures.addWarmFlowers(genBuilder);
        BiomeDefaultFeatures.addJungleGrass(genBuilder);
        BiomeDefaultFeatures.addDefaultMushrooms(genBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(genBuilder);
        BiomeDefaultFeatures.addJungleVines(genBuilder);

        final Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_JUNGLE_AND_FOREST);
        return biome(true, 0.95F, 0.9F, spawnBuilder, genBuilder, music);
    }

    private static Biome biome(boolean precipitation, float temperature, float downfall, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder generationBuilder, @Nullable Music music) {
        return biome(precipitation, temperature, downfall, 4159204, 329011, spawnBuilder, generationBuilder, music);
    }

    private static Biome biome(boolean hasPrecipitation, float temperature, float downfall, int waterColor, int waterFogColor, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder generationBuilder, @Nullable Music music) {
        return biome(hasPrecipitation, temperature, downfall, waterColor, waterFogColor, null, null, spawnBuilder, generationBuilder, music);
    }

    private static Biome biome(boolean hasPrecipitation, float temperature, float downfall, int waterColor, int waterFogColor, @Nullable Integer grassColor, @Nullable Integer leavesColor, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder genBuilder, @Nullable Music backgroundMusic) {
        final BiomeSpecialEffects.Builder builder = (new BiomeSpecialEffects.Builder())
                .waterColor(waterColor)
                .waterFogColor(waterFogColor)
                .fogColor(12638463)
                .skyColor(calculateSkyColor(temperature))
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(backgroundMusic);
        if (grassColor != null) {
            builder.grassColorOverride(grassColor);
        }

        if (leavesColor != null) {
            builder.foliageColorOverride(leavesColor);
        }

        return (new Biome.BiomeBuilder())
                .hasPrecipitation(hasPrecipitation)
                .temperature(temperature)
                .downfall(downfall)
                .specialEffects(builder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(genBuilder.build())
                .build();
    }

    protected static int calculateSkyColor(float p_194844_) {
        float $$1 = p_194844_ / 3.0F;
        $$1 = Mth.clamp($$1, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
    }

}
