package hungteen.imm.common.world.biome;

import hungteen.imm.util.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/6/2 14:23
 */
public interface IMMBiomeModifiers {

    ResourceKey<BiomeModifier> SPAWN_CULTIVATOR = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Util.prefix("spawn_cultivator"));

    static void register(BootstrapContext<BiomeModifier> context){
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
//        context.register(SPAWN_CULTIVATOR, new BiomeModifiers.AddSpawnsBiomeModifier(
//                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
//                List.of(
//                        new MobSpawnSettings.SpawnerData(IMMEntities.EMPTY_CULTIVATOR.get(), 1, 1, 1)
//                )
//        ));
    }

}
