package hungteen.imm.data;

import hungteen.imm.common.impl.codec.ElixirEffects;
import hungteen.imm.common.impl.codec.HumanSettings;
import hungteen.imm.common.impl.manuals.ManualContents;
import hungteen.imm.common.impl.manuals.SecretManuals;
import hungteen.imm.common.impl.manuals.requirments.LearnRequirements;
import hungteen.imm.common.world.levelgen.*;
import hungteen.imm.common.world.levelgen.biome.modifiers.IMMBiomeModifiers;
import hungteen.imm.common.world.levelgen.features.IMMFeatures;
import hungteen.imm.common.world.levelgen.features.IMMPlacements;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

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
//            .add(Registries.PROCESSOR_LIST, IMMProcessorLists::register)
            .add(Registries.CONFIGURED_FEATURE, IMMFeatures::register)
            .add(Registries.PLACED_FEATURE, IMMPlacements::register)
            .add(Registries.STRUCTURE, IMMStructures::register)
            .add(Registries.STRUCTURE_SET, IMMStructureSets::register)
            .add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, IMMNoiseParamLists::register)
            .add(Registries.NOISE_SETTINGS, IMMNoiseSettings::register)
            .add(Registries.LEVEL_STEM, IMMLevelStems::register)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, IMMBiomeModifiers::register)
            .add(HumanSettings.registry().getRegistryKey(), HumanSettings::register)
            .add(ElixirEffects.registry().getRegistryKey(), ElixirEffects::register)
            .add(ManualContents.registry().getRegistryKey(), ManualContents::register)
            .add(LearnRequirements.registry().getRegistryKey(), LearnRequirements::register)
            .add(SecretManuals.registry().getRegistryKey(), SecretManuals::register)
            ;

    public DatapackEntriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(Util.mc(), Util.id()));
    }

    @Override
    public String getName() {
        return Util.id() + " datapack entries";
    }
}
