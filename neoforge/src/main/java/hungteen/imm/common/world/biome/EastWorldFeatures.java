package hungteen.imm.common.world.biome;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.world.feature.IMMOrePlacements;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/6/2 9:18
 */
public class EastWorldFeatures {

    /**
     * Generations that every biome will have.
     */
    public static void globalGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    public static void addOres(BiomeGenerationSettings.Builder builder, boolean largeEmerald, boolean largeCopper) {
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_UPPER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_MIDDLE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GOLD);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GOLD_LOWER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_REDSTONE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_REDSTONE_LOWER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND_LARGE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, largeCopper ? OrePlacements.ORE_COPPER_LARGE : OrePlacements.ORE_COPPER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, largeEmerald ? IMMOrePlacements.ORE_EMERALD : IMMOrePlacements.ORE_EMERALD_SMALL);
//        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, IMMOrePlacements.ORE_CINNABAR);
    }

    public static void addSharpStake(MobSpawnSettings.Builder builder){
        builder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(IMMEntities.SHARP_STAKE.get(), 50, 1, 2));
    }

    public static void addSpiritualCultivator(MobSpawnSettings.Builder builder){
           }

    public static void addSpirit(MobSpawnSettings.Builder builder, Element...elements){
        for(Element element : elements){
            MobSpawnSettings.SpawnerData data = null;
            switch (element){
                case METAL -> data = new MobSpawnSettings.SpawnerData(IMMEntities.METAL_SPIRIT.get(), 60, 1, 1);
                case WOOD -> data = new MobSpawnSettings.SpawnerData(IMMEntities.WOOD_SPIRIT.get(), 60, 1, 1);
                case WATER -> data = new MobSpawnSettings.SpawnerData(IMMEntities.WATER_SPIRIT.get(), 40, 1, 3);
                case FIRE -> data = new MobSpawnSettings.SpawnerData(IMMEntities.WATER_SPIRIT.get(), 30, 1, 2);
                case EARTH -> data = new MobSpawnSettings.SpawnerData(IMMEntities.EARTH_SPIRIT.get(), 50, 1, 2);
            }
            if(data != null){
                builder.addSpawn(MobCategory.CREATURE, data);
            }
        }
    }

}
