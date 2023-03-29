package hungteen.imm.common.world.levelgen;

import hungteen.htlib.util.helper.registry.BiomeHelper;
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

    public static final ResourceKey<Biome> SPIRITUAL_PLAINS = register("spiritual_plains");
    public static final ResourceKey<Biome> SPIRITUAL_SAVANNA = register("spiritual_savanna");
    public static final ResourceKey<Biome> SPIRITUAL_DESERT = register("spiritual_desert");

    public static void register(BootstapContext<Biome> context){
//        register(SPIRITUAL_PLAINS, SpiritualLandBiomes.plains());
//        register(SPIRITUAL_SAVANNA, SpiritualLandBiomes.savanna());
//        register(SPIRITUAL_DESERT, SpiritualLandBiomes.desert());
    }

    private static ResourceKey<Biome> register(String name){
        return BiomeHelper.get().createKey(Util.prefix(name));
    }

}
