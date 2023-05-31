package hungteen.imm.common.world.levelgen;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 19:59
 **/
public class IMMSurfaceRules {

    private static final SurfaceRules.RuleSource AIR = makeStateRule(Blocks.AIR);
    private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
    private static final SurfaceRules.RuleSource WHITE_TERRACOTTA = makeStateRule(Blocks.WHITE_TERRACOTTA);
    private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA = makeStateRule(Blocks.ORANGE_TERRACOTTA);
    private static final SurfaceRules.RuleSource TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
    private static final SurfaceRules.RuleSource RED_SAND = makeStateRule(Blocks.RED_SAND);
    private static final SurfaceRules.RuleSource RED_SANDSTONE = makeStateRule(Blocks.RED_SANDSTONE);
    private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);
    private static final SurfaceRules.RuleSource DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource PODZOL = makeStateRule(Blocks.PODZOL);
    private static final SurfaceRules.RuleSource COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
    private static final SurfaceRules.RuleSource MYCELIUM = makeStateRule(Blocks.MYCELIUM);
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource CALCITE = makeStateRule(Blocks.CALCITE);
    private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
    private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
    private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);
    private static final SurfaceRules.RuleSource PACKED_ICE = makeStateRule(Blocks.PACKED_ICE);
    private static final SurfaceRules.RuleSource SNOW_BLOCK = makeStateRule(Blocks.SNOW_BLOCK);
    private static final SurfaceRules.RuleSource MUD = makeStateRule(Blocks.MUD);
    private static final SurfaceRules.RuleSource POWDER_SNOW = makeStateRule(Blocks.POWDER_SNOW);
    private static final SurfaceRules.RuleSource ICE = makeStateRule(Blocks.ICE);
    private static final SurfaceRules.RuleSource WATER = makeStateRule(Blocks.WATER);
    private static final SurfaceRules.RuleSource LAVA = makeStateRule(Blocks.LAVA);
    private static final SurfaceRules.RuleSource NETHERRACK = makeStateRule(Blocks.NETHERRACK);
    private static final SurfaceRules.RuleSource SOUL_SAND = makeStateRule(Blocks.SOUL_SAND);
    private static final SurfaceRules.RuleSource SOUL_SOIL = makeStateRule(Blocks.SOUL_SOIL);
    private static final SurfaceRules.RuleSource BASALT = makeStateRule(Blocks.BASALT);
    private static final SurfaceRules.RuleSource BLACKSTONE = makeStateRule(Blocks.BLACKSTONE);
    private static final SurfaceRules.RuleSource WARPED_WART_BLOCK = makeStateRule(Blocks.WARPED_WART_BLOCK);
    private static final SurfaceRules.RuleSource WARPED_NYLIUM = makeStateRule(Blocks.WARPED_NYLIUM);
    private static final SurfaceRules.RuleSource NETHER_WART_BLOCK = makeStateRule(Blocks.NETHER_WART_BLOCK);
    private static final SurfaceRules.RuleSource CRIMSON_NYLIUM = makeStateRule(Blocks.CRIMSON_NYLIUM);
    private static final SurfaceRules.RuleSource ENDSTONE = makeStateRule(Blocks.END_STONE);

    public static SurfaceRules.RuleSource overworldLike(boolean has_roof, boolean has_floor) {
        SurfaceRules.ConditionSource conditionsource = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(97), 2);
        SurfaceRules.ConditionSource conditionsource1 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
        SurfaceRules.ConditionSource conditionsource2 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(63), -1);
        SurfaceRules.ConditionSource conditionsource3 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(74), 1);
        SurfaceRules.ConditionSource conditionsource4 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(60), 0);
        SurfaceRules.ConditionSource conditionsource5 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
        SurfaceRules.ConditionSource conditionsource6 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
        // 在水上面。
        SurfaceRules.ConditionSource noWaterAboveFloor = SurfaceRules.waterBlockCheck(-1, 0);
        // 在水上面的上面。
        SurfaceRules.ConditionSource conditionsource8 = SurfaceRules.waterBlockCheck(0, 0);
        SurfaceRules.ConditionSource conditionsource9 = SurfaceRules.waterStartCheck(-6, -1);
        SurfaceRules.ConditionSource hole = SurfaceRules.hole();
        SurfaceRules.ConditionSource frozenOcean = SurfaceRules.isBiome(Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
        SurfaceRules.ConditionSource steep = SurfaceRules.steep();
        /* Place GrassBlock and Dirt */
        SurfaceRules.RuleSource putDirtOrGrass = SurfaceRules.sequence(SurfaceRules.ifTrue(conditionsource8, GRASS_BLOCK), DIRT);
        SurfaceRules.RuleSource putSandOrStone = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND);
        SurfaceRules.RuleSource putStoneOrGravel = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL);
        SurfaceRules.ConditionSource warmOceanOrBeach = SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.BEACH, Biomes.SNOWY_BEACH);
        SurfaceRules.ConditionSource desert = SurfaceRules.isBiome(IMMBiomes.DESERT);
        SurfaceRules.RuleSource rulesource3 = SurfaceRules.sequence(
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.STONY_PEAKS),
//                        SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.CALCITE, -0.0125D, 0.0125D), CALCITE), STONE)
//                ),
//                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.STONY_SHORE),
//                        SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.GRAVEL, -0.05D, 0.05D), putStoneOrGravel), STONE)
//                ),
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.WINDSWEPT_HILLS),
//                        SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE)
//                ),
                SurfaceRules.ifTrue(warmOceanOrBeach, putSandOrStone),
                SurfaceRules.ifTrue(desert, putSandOrStone)
//                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.DRIPSTONE_CAVES), STONE)
        );
        SurfaceRules.RuleSource putPowderSnow = SurfaceRules.ifTrue(
                SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45D, 0.58D),
                SurfaceRules.ifTrue(conditionsource8, POWDER_SNOW)
        );
        SurfaceRules.RuleSource putPowderSnow2 = SurfaceRules.ifTrue(
                SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.35D, 0.6D),
                SurfaceRules.ifTrue(conditionsource8, POWDER_SNOW)
        );
        SurfaceRules.RuleSource rulesource6 = SurfaceRules.sequence(
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.FROZEN_PEAKS),
//                        SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(steep, PACKED_ICE),
//                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, -0.5D, 0.2D), PACKED_ICE),
//                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, -0.0625D, 0.025D), ICE),
//                                SurfaceRules.ifTrue(conditionsource8, SNOW_BLOCK)
//                        )
//                ), SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.SNOWY_SLOPES),
//                        SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(steep, STONE),
//                                putPowderSnow,
//                                SurfaceRules.ifTrue(conditionsource8, SNOW_BLOCK)
//                        )
//                ),
//                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.JAGGED_PEAKS), STONE),
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.GROVE),
//                        SurfaceRules.sequence(putPowderSnow, DIRT)
//                ),
                rulesource3,
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA),
//                        SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE)
//                ),
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS),
//                        SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), putStoneOrGravel),
//                                SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE),
//                                SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), DIRT),
//                                putStoneOrGravel
//                        )
//                ),
//                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MANGROVE_SWAMP), MUD),
                DIRT
        );
        SurfaceRules.RuleSource rulesource7 = SurfaceRules.sequence(
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.FROZEN_PEAKS),
//                        SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(steep, PACKED_ICE),
//                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, 0.0D, 0.2D), PACKED_ICE),
//                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, 0.0D, 0.025D), ICE),
//                                SurfaceRules.ifTrue(conditionsource8, SNOW_BLOCK)
//                        )
//                ),
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.SNOWY_SLOPES),
//                        SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(steep, STONE),
//                                putPowderSnow2,
//                                SurfaceRules.ifTrue(conditionsource8, SNOW_BLOCK)
//                        )
//                ),
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.JAGGED_PEAKS),
//                        SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(steep, STONE),
//                                SurfaceRules.ifTrue(conditionsource8, SNOW_BLOCK)
//                        )
//                ),
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.GROVE),
//                        SurfaceRules.sequence(
//                                putPowderSnow2,
//                                SurfaceRules.ifTrue(conditionsource8, SNOW_BLOCK)
//                        )
//                ),
                rulesource3,
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA),
//                        SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE),
//                                SurfaceRules.ifTrue(surfaceNoiseAbove(-0.5D), COARSE_DIRT)
//                        )
//                ),
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS),
//                        SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), putStoneOrGravel),
//                                SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE),
//                                SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), putDirtOrGrass),
//                                putStoneOrGravel
//                        )
//                ),
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA),
//                        SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), COARSE_DIRT),
//                                SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95D), PODZOL)
//                        )
//                ),
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.ICE_SPIKES),
//                        SurfaceRules.ifTrue(conditionsource8, SNOW_BLOCK)
//                ),
//                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MANGROVE_SWAMP), MUD),
//                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MUSHROOM_FIELDS), MYCELIUM),
                putDirtOrGrass
        );
        SurfaceRules.ConditionSource conditionsource15 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.909D, -0.5454D);
        SurfaceRules.ConditionSource conditionsource16 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.1818D, 0.1818D);
        SurfaceRules.ConditionSource conditionsource17 = SurfaceRules.noiseCondition(Noises.SURFACE, 0.5454D, 0.909D);
        SurfaceRules.RuleSource resultSource = SurfaceRules.sequence(
//                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
//                        SurfaceRules.sequence(
//                                // 恶地的坏土。
//                                SurfaceRules.ifTrue(
//                                        SurfaceRules.isBiome(Biomes.WOODED_BADLANDS),
//                                        SurfaceRules.ifTrue(conditionsource,
//                                                SurfaceRules.sequence(
//                                                        SurfaceRules.ifTrue(conditionsource15, COARSE_DIRT),
//                                                        SurfaceRules.ifTrue(conditionsource16, COARSE_DIRT),
//                                                        SurfaceRules.ifTrue(conditionsource17, COARSE_DIRT),
//                                                        putDirtOrGrass)
//                                        )
//                                ),
//                                // 沼泽的水。
//                                SurfaceRules.ifTrue(
//                                        SurfaceRules.isBiome(Biomes.SWAMP),
//                                        SurfaceRules.ifTrue(conditionsource5,
//                                                SurfaceRules.ifTrue(
//                                                        SurfaceRules.not(conditionsource6),
//                                                        SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER)
//                                                )
//                                        )
//                                ),
                                  // 沼泽的水。
//                                SurfaceRules.ifTrue(
//                                        SurfaceRules.isBiome(Biomes.MANGROVE_SWAMP),
//                                        SurfaceRules.ifTrue(conditionsource4,
//                                                SurfaceRules.ifTrue(
//                                                        SurfaceRules.not(conditionsource6),
//                                                        SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER)
//                                                )
//                                        )
//                                )
//                        )
//                ),
                //恶地类群系相关。
//                SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS),
//                        SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
//                                        SurfaceRules.sequence(
//                                                SurfaceRules.ifTrue(conditionsource1, ORANGE_TERRACOTTA),
//                                                SurfaceRules.ifTrue(conditionsource3,
//                                                        SurfaceRules.sequence(
//                                                                SurfaceRules.ifTrue(conditionsource15, TERRACOTTA),
//                                                                SurfaceRules.ifTrue(conditionsource16, TERRACOTTA),
//                                                                SurfaceRules.ifTrue(conditionsource17, TERRACOTTA),
//                                                                SurfaceRules.bandlands()
//                                                        )
//                                                ), SurfaceRules.ifTrue(noWaterAboveFloor,
//                                                        SurfaceRules.sequence(
//                                                                SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, RED_SANDSTONE),
//                                                                RED_SAND)
//                                                ),
//                                                SurfaceRules.ifTrue(SurfaceRules.not(hole), ORANGE_TERRACOTTA),
//                                                SurfaceRules.ifTrue(conditionsource9, WHITE_TERRACOTTA), putStoneOrGravel)
//                                ), SurfaceRules.ifTrue(conditionsource2,
//                                        SurfaceRules.sequence(
//                                                SurfaceRules.ifTrue(conditionsource6,
//                                                        SurfaceRules.ifTrue(SurfaceRules.not(conditionsource3), ORANGE_TERRACOTTA)
//                                                ),
//                                                SurfaceRules.bandlands()
//                                        )
//                                ), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR,
//                                        SurfaceRules.ifTrue(conditionsource9, WHITE_TERRACOTTA)
//                                )
//                        )
//                ),
                // 地表（上面不能有水）。
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.ifTrue(noWaterAboveFloor,
                                SurfaceRules.sequence(
                                        SurfaceRules.ifTrue(frozenOcean,
                                                SurfaceRules.ifTrue(hole,
                                                        SurfaceRules.sequence(
                                                                SurfaceRules.ifTrue(conditionsource8, AIR),
                                                                SurfaceRules.ifTrue(SurfaceRules.temperature(), ICE),
                                                                WATER
                                                        )
                                                )
                                        ),
                                        rulesource7
                                )
                        )
                ),
                // 沙石？水？
                SurfaceRules.ifTrue(conditionsource9,
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                        SurfaceRules.ifTrue(frozenOcean,
                                                SurfaceRules.ifTrue(hole, WATER)
                                        )
                                ),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, rulesource6),
                                SurfaceRules.ifTrue(warmOceanOrBeach,
                                        SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, SANDSTONE)
                                ),
                                SurfaceRules.ifTrue(desert,
                                        SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, SANDSTONE)
                                )
                        )
                ),
                // 山峰换成石头，温带海洋换成沙石和沙子，其他的换成石头和砂砾。
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS), STONE),
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN), putSandOrStone),
                                putStoneOrGravel
                        )
                )
        );

        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        if (has_roof) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK));
        }
        if (has_floor) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
        }
        builder.add(SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), resultSource));
        builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }

    private static SurfaceRules.ConditionSource surfaceNoiseAbove(double p_194809_) {
        return SurfaceRules.noiseCondition(Noises.SURFACE, p_194809_ / 8.25D, Double.MAX_VALUE);
    }
}
