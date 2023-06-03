package hungteen.imm.common.world.levelgen;

import hungteen.htlib.util.helper.registry.BiomeHelper;
import hungteen.imm.common.world.levelgen.biome.EastWorldBiomes;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-18 14:03
 *
 * Look at {@link Biomes}
 **/
public class IMMBiomes {

    public static final ResourceKey<Biome> PLAINS = create("plains");
    public static final ResourceKey<Biome> SAVANNA = create("savanna");
    public static final ResourceKey<Biome> DESERT = create("desert");
    public static final ResourceKey<Biome> BAMBOO_JUNGLE = create("bamboo_jungle");

    public static void register(BootstapContext<Biome> context){
        EastWorldBiomes.initBiomes(context);
    }

    private static ResourceKey<Biome> create(String name){
        return BiomeHelper.get().createKey(Util.prefix(name));
    }

}
