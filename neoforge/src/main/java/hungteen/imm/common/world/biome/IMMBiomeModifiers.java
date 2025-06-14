package hungteen.imm.common.world.biome;

import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.tag.IMMBiomeTags;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/6/2 14:23
 */
public interface IMMBiomeModifiers {

    ResourceKey<BiomeModifier> SPAWN_CULTIVATOR = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Util.prefix("spawn_cultivator"));
    ResourceKey<BiomeModifier> SPAWN_SHARP_STAKE = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Util.prefix("spawn_sharp_stake"));
    ResourceKey<BiomeModifier> SPAWN_CHILLAGER = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Util.prefix("spawn_chillager"));
    ResourceKey<BiomeModifier> SPAWN_QI_MOB = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Util.prefix("spawn_qi_mob"));

    static void register(BootstrapContext<BiomeModifier> context){
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
//        context.postRegister(SPAWN_CULTIVATOR, new BiomeModifiers.AddSpawnsBiomeModifier(
//                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
//                List.of(
//                        new MobSpawnSettings.SpawnerData(IMMEntities.EMPTY_CULTIVATOR.get(), 1, 1, 1)
//                )
//        ));
        context.register(SPAWN_SHARP_STAKE, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_FOREST),
                List.of(
                        new MobSpawnSettings.SpawnerData(IMMEntities.SHARP_STAKE.get(), 10, 1, 1)
                )
        ));
        context.register(SPAWN_CHILLAGER, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_COLD_OVERWORLD),
                List.of(
                        new MobSpawnSettings.SpawnerData(IMMEntities.CHILLAGER.get(), 10, 1, 1)
                )
        ));
        context.register(SPAWN_QI_MOB, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(IMMBiomeTags.CAN_SPAWN_MOB),
                List.of(
                        new MobSpawnSettings.SpawnerData(IMMEntities.CHILLAGER.get(), 10, 1, 1)
                )
        ));
    }

}
