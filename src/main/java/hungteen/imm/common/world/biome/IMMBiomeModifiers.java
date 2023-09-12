package hungteen.imm.common.world.biome;

import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/2 14:23
 */
public interface IMMBiomeModifiers {

    ResourceKey<BiomeModifier> SPAWN_CULTIVATOR = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, Util.prefix("spawn_cultivator"));

    static void register(BootstapContext<BiomeModifier> context){
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        context.register(SPAWN_CULTIVATOR, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                List.of(
                        new MobSpawnSettings.SpawnerData(IMMEntities.EMPTY_CULTIVATOR.get(), 1, 1, 1)
                )
        ));
    }

}
