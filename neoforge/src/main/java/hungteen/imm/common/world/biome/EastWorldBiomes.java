package hungteen.imm.common.world.biome;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.world.feature.IMMVegetationPlacements;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
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
import java.util.function.Consumer;

/**
 * Look at {@link net.minecraft.data.worldgen.biome.OverworldBiomes}.
 *
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-18 14:11
 **/
public class EastWorldBiomes {

    public static void initBiomes(BootstrapContext<Biome> context) {
        final HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        final HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);
        context.register(IMMBiomes.PLAINS, plains(features, carvers, false, false));
        context.register(IMMBiomes.SAVANNA, savanna(features, carvers));
        context.register(IMMBiomes.DESERT, desert(features, carvers));
        context.register(IMMBiomes.BAMBOO_JUNGLE, bambooJungle(features, carvers));
        context.register(IMMBiomes.MEADOW, meadow(features, carvers));
        context.register(IMMBiomes.CUT_BIRCH_FOREST, birchForest(features, carvers));
        context.register(IMMBiomes.CUT_DARK_FOREST, darkForest(features, carvers));
    }

    /**
     * Tall Grass patch.
     * Rush Cow.
     * Mulberry Tree.
     * Silk Worm.
     * Wild Silk Worm.
     */
    private static Biome plains(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean sunflower, boolean snowy) {
        final MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        if (snowy) {
            spawnBuilder.creatureGenerationProbability(0.07F);
            BiomeDefaultFeatures.snowySpawns(spawnBuilder);
        } else {
            BiomeDefaultFeatures.plainsSpawns(spawnBuilder);
        }
        EastWorldFeatures.addSpiritualCultivator(spawnBuilder);
        EastWorldFeatures.addSpirit(spawnBuilder, Element.WATER, Element.EARTH);

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

    /**
     * Village Kingdom Base.
     */
    public static Biome meadow(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        final BiomeGenerationSettings.Builder genBuilder = new BiomeGenerationSettings.Builder(features, carvers);
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
//        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 1, 2)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 2, 6)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 2, 2, 4));
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);
        EastWorldFeatures.addSpiritualCultivator(spawnBuilder);
        EastWorldFeatures.addSpirit(spawnBuilder, Element.WATER, Element.EARTH);

        EastWorldFeatures.globalGeneration(genBuilder);
        EastWorldFeatures.addOres(genBuilder, false, false);
        BiomeDefaultFeatures.addDefaultSoftDisks(genBuilder);
        BiomeDefaultFeatures.addPlainGrass(genBuilder);
        BiomeDefaultFeatures.addMeadowVegetation(genBuilder);

        BiomeDefaultFeatures.addExtraEmeralds(genBuilder);
        BiomeDefaultFeatures.addInfestedStone(genBuilder);
        Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_MEADOW);
        return biome(true, 0.5F, 0.8F, builder -> builder.waterColor(937679).waterFogColor(329011), spawnBuilder, genBuilder, music);
    }

    /**
     * BiFang Bird.
     */
    private static Biome savanna(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        final MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
//        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.RAVAGER, 1, 1, 1));
        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);
        EastWorldFeatures.addSpiritualCultivator(spawnBuilder);
        EastWorldFeatures.addSpirit(spawnBuilder, Element.WOOD, Element.FIRE);

        final BiomeGenerationSettings.Builder genBuilder = new BiomeGenerationSettings.Builder(features, carvers);
        EastWorldFeatures.globalGeneration(genBuilder);
        EastWorldFeatures.addOres(genBuilder, false, false);
        BiomeDefaultFeatures.addDefaultSoftDisks(genBuilder);
        BiomeDefaultFeatures.addWarmFlowers(genBuilder);
        BiomeDefaultFeatures.addSavannaExtraGrass(genBuilder);
        BiomeDefaultFeatures.addSavannaGrass(genBuilder);
        BiomeDefaultFeatures.addDefaultMushrooms(genBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(genBuilder);

        return biome(false, 2.0F, 0.0F, spawnBuilder, genBuilder, null);
    }

    /**
     * Pillager Base.
     * Ravager.
     */
    private static Biome desert(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        final MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3));
        EastWorldFeatures.addSpiritualCultivator(spawnBuilder);
        EastWorldFeatures.addSpirit(spawnBuilder, Element.METAL, Element.FIRE, Element.EARTH);

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

    /**
     * {@link net.minecraft.data.worldgen.biome.OverworldBiomes#forest(HolderGetter, HolderGetter, boolean, boolean, boolean)}
     */
    public static Biome birchForest(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        final MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);
        EastWorldFeatures.addSharpStake(spawnBuilder);
        EastWorldFeatures.addSpiritualCultivator(spawnBuilder);
        EastWorldFeatures.addSpirit(spawnBuilder, Element.METAL, Element.WOOD);

        final BiomeGenerationSettings.Builder genBuilder = new BiomeGenerationSettings.Builder(features, carvers);
        EastWorldFeatures.globalGeneration(genBuilder);
        genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FOREST_FLOWERS);
        EastWorldFeatures.addOres(genBuilder, false, false);
        BiomeDefaultFeatures.addDefaultSoftDisks(genBuilder);
        BiomeDefaultFeatures.addDefaultFlowers(genBuilder);
        BiomeDefaultFeatures.addForestGrass(genBuilder);
        genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, IMMVegetationPlacements.TREES_BIRCH_FOREST);
        genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, IMMVegetationPlacements.BIRCH_STAKE);
        genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, IMMVegetationPlacements.BIRCH_HORIZONTAL_STAKE);
        BiomeDefaultFeatures.addDefaultMushrooms(genBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(genBuilder);

        final Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FLOWER_FOREST);
        return biome(true, 0.6F, 0.6F, spawnBuilder, genBuilder, music);
    }

    /**
     * Tree House above leaves.
     * Tall Dark Oak Tree.
     * Poison Spider.
     * Ganoderma.
     * Hunter.
     */
    public static Biome darkForest(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        final MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);
        EastWorldFeatures.addSharpStake(spawnBuilder);
        EastWorldFeatures.addSpiritualCultivator(spawnBuilder);

        final BiomeGenerationSettings.Builder genBuilder = new BiomeGenerationSettings.Builder(features, carvers);
        EastWorldFeatures.globalGeneration(genBuilder);
        EastWorldFeatures.addOres(genBuilder, true, false);
        genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, IMMVegetationPlacements.TREES_DARK_FOREST);
        genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, IMMVegetationPlacements.DARK_OAK_STAKE);
        genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, IMMVegetationPlacements.DARK_OAK_HORIZONTAL_STAKE);
        BiomeDefaultFeatures.addForestFlowers(genBuilder);
        BiomeDefaultFeatures.addDefaultSoftDisks(genBuilder);
        BiomeDefaultFeatures.addDefaultFlowers(genBuilder);
        BiomeDefaultFeatures.addForestGrass(genBuilder);
        BiomeDefaultFeatures.addDefaultMushrooms(genBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(genBuilder);
        genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, IMMVegetationPlacements.GANODERMA_DARK_FOREST);

        final Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST);
        return biome(true, 0.7F, 0.8F, builder -> {
            builder.grassColorModifier(BiomeSpecialEffects.GrassColorModifier.DARK_FOREST);
        }, spawnBuilder, genBuilder, music);
    }

    private static Biome bambooJungle(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.baseJungleSpawns(spawnBuilder);
        EastWorldFeatures.addSharpStake(spawnBuilder);
        EastWorldFeatures.addSpiritualCultivator(spawnBuilder);
        EastWorldFeatures.addSpirit(spawnBuilder, Element.METAL, Element.WOOD);

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

        final Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_JUNGLE);
        return biome(true, 0.95F, 0.9F, spawnBuilder, genBuilder, music);
    }

    private static Biome biome(boolean precipitation, float temperature, float downfall, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder generationBuilder, @Nullable Music music) {
        return biome(precipitation, temperature, downfall, builder -> {
        }, spawnBuilder, generationBuilder, music);
    }

    private static Biome biome(boolean hasPrecipitation, float temperature, float downfall, Consumer<BiomeSpecialEffects.Builder> consumer, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder genBuilder, @Nullable Music backgroundMusic) {
        final BiomeSpecialEffects.Builder builder = (new BiomeSpecialEffects.Builder())
                .waterColor(4159204)
                .waterFogColor(329011)
                .fogColor(12638463)
                .skyColor(calculateSkyColor(temperature))
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(backgroundMusic);
        consumer.accept(builder);

        return (new Biome.BiomeBuilder())
                .hasPrecipitation(hasPrecipitation)
                .temperature(temperature)
                .downfall(downfall)
                .specialEffects(builder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(genBuilder.build())
                .build();
    }

    protected static int calculateSkyColor(float temperature) {
        float $$1 = temperature / 3.0F;
        $$1 = Mth.clamp($$1, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
    }

}
