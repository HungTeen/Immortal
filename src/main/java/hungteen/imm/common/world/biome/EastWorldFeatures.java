package hungteen.imm.common.world.biome;

import hungteen.imm.common.world.feature.IMMOrePlacements;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/2 9:18
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
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, IMMOrePlacements.ORE_CINNABAR);
    }

}
