package hungteen.imm.data;

import hungteen.htlib.common.impl.position.HTPositionComponents;
import hungteen.htlib.common.impl.raid.HTRaidComponents;
import hungteen.htlib.common.impl.result.HTResultComponents;
import hungteen.htlib.common.impl.spawn.HTSpawnComponents;
import hungteen.htlib.common.impl.wave.HTWaveComponents;
import hungteen.imm.common.impl.codec.ElixirEffects;
import hungteen.imm.common.entity.human.setting.HumanSettings;
import hungteen.imm.common.impl.manuals.SecretManuals;
import hungteen.imm.common.impl.raid.*;
import hungteen.imm.common.misc.damage.IMMDamageTypes;
import hungteen.imm.common.world.levelgen.*;
import hungteen.imm.common.world.biome.IMMBiomes;
import hungteen.imm.common.world.biome.IMMBiomeModifiers;
import hungteen.imm.common.world.feature.IMMFeatures;
import hungteen.imm.common.world.feature.IMMPlacements;
import hungteen.imm.common.world.structure.IMMProcessorLists;
import hungteen.imm.common.world.structure.IMMStructureSets;
import hungteen.imm.common.world.structure.IMMStructures;
import hungteen.imm.common.world.structure.IMMTemplatePools;
import hungteen.imm.data.tag.BiomeTagGen;
import hungteen.imm.data.tag.DamageTypeTagGen;
import hungteen.imm.data.tag.StructureTagGen;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-20 15:16
 **/
public class DatapackEntriesGen extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            // Vanilla.
            .add(Registries.DAMAGE_TYPE, IMMDamageTypes::register)
            .add(Registries.DIMENSION_TYPE, IMMDimensionTypes::register)
            .add(Registries.BIOME, IMMBiomes::register)
            .add(Registries.PROCESSOR_LIST, IMMProcessorLists::register)
            .add(Registries.TEMPLATE_POOL, IMMTemplatePools::register)
            .add(Registries.CONFIGURED_FEATURE, IMMFeatures::register)
            .add(Registries.PLACED_FEATURE, IMMPlacements::register)
            .add(Registries.STRUCTURE, IMMStructures::register)
            .add(Registries.STRUCTURE_SET, IMMStructureSets::register)
            .add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, IMMNoiseParamLists::register)
            .add(Registries.NOISE_SETTINGS, IMMNoiseSettings::register)
            .add(Registries.LEVEL_STEM, IMMLevelStems::register)
            // Forge.
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, IMMBiomeModifiers::register)
            // HTLib.
            .add(HTPositionComponents.registry().getRegistryKey(), IMMPositionComponents::register)
            .add(HTResultComponents.registry().getRegistryKey(), IMMResultComponents::register)
            .add(HTSpawnComponents.registry().getRegistryKey(), IMMSpawnComponents::register)
            .add(HTWaveComponents.registry().getRegistryKey(), IMMWaveComponents::register)
            .add(HTRaidComponents.registry().getRegistryKey(), IMMRaidComponents::register)
            // Immortal.
            .add(HumanSettings.registry().getRegistryKey(), HumanSettings::register)
            .add(ElixirEffects.registry().getRegistryKey(), ElixirEffects::register)
            .add(SecretManuals.registry().getRegistryKey(), SecretManuals::register)
            ;

    public DatapackEntriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(Util.mc().getModID(), Util.id()));
    }

    public static void addProviders(boolean isServer, DataGenerator generator, PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        generator.addProvider(isServer, new DatapackEntriesGen(output, provider));
        // This is needed here because Minecraft Forge doesn't properly support tagging custom registries, without problems.
        // If you think this looks fixable, please ensure the fixes are tested in runData & runClient as these current issues exist entirely within Forge's internals.
        generator.addProvider(isServer, new BiomeTagGen(output, provider.thenApply(DatapackEntriesGen::append), helper));
        generator.addProvider(isServer, new StructureTagGen(output, provider.thenApply(DatapackEntriesGen::append), helper));
        generator.addProvider(isServer, new DamageTypeTagGen(output, provider.thenApply(DatapackEntriesGen::append), helper));
    }

    private static HolderLookup.Provider append(HolderLookup.Provider original) {
        return BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }

    @Override
    public String getName() {
        return Util.id() + " datapack entries";
    }
}
