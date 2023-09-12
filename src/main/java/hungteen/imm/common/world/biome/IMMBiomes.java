package hungteen.imm.common.world.biome;

import hungteen.htlib.util.helper.registry.BiomeHelper;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-18 14:03
 **/
public interface IMMBiomes {

    ResourceKey<Biome> PLAINS = create("plains");
    ResourceKey<Biome> SAVANNA = create("savanna");
    ResourceKey<Biome> DESERT = create("desert");
    ResourceKey<Biome> BAMBOO_JUNGLE = create("bamboo_jungle");
    ResourceKey<Biome> MEADOW = create("meadow");
    ResourceKey<Biome> BIRCH_FOREST = create("birch_forest");
    ResourceKey<Biome> CUT_BIRCH_FOREST = create("cut_birch_forest");

    /**
     * Look at {@link net.minecraft.world.level.biome.Biomes}.
     */
    static void register(BootstapContext<Biome> context){
        EastWorldBiomes.initBiomes(context);
    }

    static ResourceKey<Biome> create(String name){
        return BiomeHelper.get().createKey(Util.prefix(name));
    }

}
