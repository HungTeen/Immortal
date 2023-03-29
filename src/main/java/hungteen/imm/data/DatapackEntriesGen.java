package hungteen.imm.data;

import hungteen.imm.common.world.levelgen.*;
import hungteen.imm.common.world.levelgen.feature.IMMPlacedFeatures;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-20 15:16
 **/
public class DatapackEntriesGen extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, IMMDimensionTypes::register)
            .add(Registries.BIOME, IMMBiomes::register)
            .add(Registries.PROCESSOR_LIST, IMMProcessorLists::register)
            .add(Registries.PLACED_FEATURE, IMMPlacedFeatures::register)
            .add(Registries.STRUCTURE, IMMStructures::register)
            .add(Registries.STRUCTURE_SET, IMMStructureSets::register)
            .add(Registries.NOISE_SETTINGS, IMMNoiseSettings::register)
            .add(Registries.LEVEL_STEM, IMMLevelStems::register);

    public DatapackEntriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(Util.mc(), Util.forge(), Util.id()));
    }

    @Override
    public String getName() {
        return Util.id() + " datapack entries";
    }
}
