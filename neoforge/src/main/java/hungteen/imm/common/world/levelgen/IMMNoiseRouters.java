package hungteen.imm.common.world.levelgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.stream.Stream;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-09-03 11:00
 **/
public interface IMMNoiseRouters {

    ResourceKey<DensityFunction> Y = createKey("y");
    ResourceKey<DensityFunction> SHIFT_X = createKey("shift_x");
    ResourceKey<DensityFunction> SHIFT_Z = createKey("shift_z");
    ResourceKey<DensityFunction> SLOPED_CHEESE = createKey("overworld/sloped_cheese");
    ResourceKey<DensityFunction> ENTRANCES = createKey("overworld/caves/entrances");
    ResourceKey<DensityFunction> NOODLE = createKey("overworld/caves/noodle");

    /**
     * {@link NoiseRouterData#overworld(HolderGetter, HolderGetter, boolean, boolean)}
     */
    static NoiseRouter overworld(HolderGetter<DensityFunction> densities, HolderGetter<NormalNoise.NoiseParameters> parameters) {
        final DensityFunction barrier = DensityFunctions.noise(parameters.getOrThrow(Noises.AQUIFER_BARRIER), 0.5D);
        final DensityFunction fluidLevelFlood = DensityFunctions.noise(parameters.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67D);
        final DensityFunction fluidLevelSpread = DensityFunctions.noise(parameters.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143D);
        final DensityFunction lava = DensityFunctions.noise(parameters.getOrThrow(Noises.AQUIFER_LAVA));
        final DensityFunction shiftX = getFunction(densities, SHIFT_X);
        final DensityFunction shiftZ = getFunction(densities, SHIFT_Z);
        // Change xz scale from 0.25 to 0.3
        final DensityFunction temperature = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.3D, parameters.getOrThrow(Noises.TEMPERATURE));
        // Change xz scale from 0.25 to 0.2
        final DensityFunction vegetation = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.2D, parameters.getOrThrow(Noises.VEGETATION));
        final DensityFunction factor = getFunction(densities, NoiseRouterData.FACTOR);
        final DensityFunction depth = getFunction(densities, NoiseRouterData.DEPTH);
        final DensityFunction densityfunction10 = noiseGradientDensity(DensityFunctions.cache2d(factor), depth);
        final DensityFunction slopedCheese = getFunction(densities, SLOPED_CHEESE);
        final DensityFunction densityfunction12 = DensityFunctions.min(slopedCheese, DensityFunctions.mul(DensityFunctions.constant(5.0D), getFunction(densities, ENTRANCES)));
        final DensityFunction densityfunction13 = DensityFunctions.rangeChoice(slopedCheese, -1000000.0D, 1.5625D, densityfunction12, NoiseRouterData.underground(densities, parameters, slopedCheese));
        final DensityFunction finalDensity = DensityFunctions.min(postProcess(slideOverworld(false, densityfunction13)), getFunction(densities, NOODLE));
        final DensityFunction densityfunction15 = getFunction(densities, Y);
        final int i = Stream.of(OreVeinifier.VeinType.values()).mapToInt((p_224495_) -> {
            return p_224495_.minY;
        }).min().orElse(-DimensionType.MIN_Y * 2);
        final int j = Stream.of(OreVeinifier.VeinType.values()).mapToInt((p_224457_) -> {
            return p_224457_.maxY;
        }).max().orElse(-DimensionType.MIN_Y * 2);
        final DensityFunction veinToggle = yLimitedInterpolatable(densityfunction15, DensityFunctions.noise(parameters.getOrThrow(Noises.ORE_VEININESS), 1.5D, 1.5D), i, j, 0);
        final DensityFunction densityfunction17 = yLimitedInterpolatable(densityfunction15, DensityFunctions.noise(parameters.getOrThrow(Noises.ORE_VEIN_A), 4.0D, 4.0D), i, j, 0).abs();
        final DensityFunction densityfunction18 = yLimitedInterpolatable(densityfunction15, DensityFunctions.noise(parameters.getOrThrow(Noises.ORE_VEIN_B), 4.0D, 4.0D), i, j, 0).abs();
        final DensityFunction veinRidged = DensityFunctions.add(DensityFunctions.constant(-0.08F), DensityFunctions.max(densityfunction17, densityfunction18));
        final DensityFunction veinGap = DensityFunctions.noise(parameters.getOrThrow(Noises.ORE_GAP));
        return new NoiseRouter(
                barrier,
                fluidLevelFlood,
                fluidLevelSpread,
                lava,
                temperature,
                vegetation,
                getFunction(densities, NoiseRouterData.CONTINENTS),
                getFunction(densities, NoiseRouterData.EROSION),
                depth,
                getFunction(densities, NoiseRouterData.RIDGES),
                slideOverworld(false, DensityFunctions.add(densityfunction10, DensityFunctions.constant(-0.703125D)).clamp(-64.0D, 64.0D)),
                finalDensity,
                veinToggle,
                veinRidged,
                veinGap
        );
    }

    private static DensityFunction slideOverworld(boolean p_224490_, DensityFunction p_224491_) {
        return slide(p_224491_, -64, 384, p_224490_ ? 16 : 80, p_224490_ ? 0 : 64, -0.078125D, 0, 24, p_224490_ ? 0.4D : 0.1171875D);
    }

    private static DensityFunction slide(DensityFunction p_224444_, int p_224445_, int p_224446_, int p_224447_, int p_224448_, double p_224449_, int p_224450_, int p_224451_, double p_224452_) {
        DensityFunction densityfunction1 = DensityFunctions.yClampedGradient(p_224445_ + p_224446_ - p_224447_, p_224445_ + p_224446_ - p_224448_, 1.0D, 0.0D);
        DensityFunction $$9 = DensityFunctions.lerp(densityfunction1, p_224449_, p_224444_);
        DensityFunction densityfunction2 = DensityFunctions.yClampedGradient(p_224445_ + p_224450_, p_224445_ + p_224451_, 0.0D, 1.0D);
        return DensityFunctions.lerp(densityfunction2, p_224452_, $$9);
    }

    private static DensityFunction yLimitedInterpolatable(DensityFunction p_209472_, DensityFunction p_209473_, int p_209474_, int p_209475_, int p_209476_) {
        return DensityFunctions.interpolated(DensityFunctions.rangeChoice(p_209472_, p_209474_, p_209475_ + 1, p_209473_, DensityFunctions.constant(p_209476_)));
    }

    private static DensityFunction postProcess(DensityFunction densityFunction) {
        DensityFunction densityfunction = DensityFunctions.blendDensity(densityFunction);
        return DensityFunctions.mul(DensityFunctions.interpolated(densityfunction), DensityFunctions.constant(0.64D)).squeeze();
    }

    private static DensityFunction noiseGradientDensity(DensityFunction p_212272_, DensityFunction p_212273_) {
        DensityFunction densityfunction = DensityFunctions.mul(p_212273_, p_212272_);
        return DensityFunctions.mul(DensityFunctions.constant(4.0D), densityfunction.quarterNegative());
    }

    private static DensityFunction getFunction(HolderGetter<DensityFunction> densities, ResourceKey<DensityFunction> key) {
        return new DensityFunctions.HolderHolder(densities.getOrThrow(key));
    }

    private static ResourceKey<DensityFunction> createKey(String name) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.parse(name));
    }
}
