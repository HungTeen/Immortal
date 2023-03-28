package hungteen.imm.common.world.biome;

import hungteen.htlib.util.helper.registry.BiomeHelper;
import hungteen.imm.utils.Util;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-18 14:03
 *
 * Look at {@link Biomes}
 **/
public class ImmortalBiomes {

    private static final Map<ResourceKey<Biome>, Biome> BIOME_MAP = new HashMap<>();

    public static final ResourceKey<Biome> SPIRITUAL_PLAINS = register("spiritual_plains");
    public static final ResourceKey<Biome> SPIRITUAL_SAVANNA = register("spiritual_savanna");
    public static final ResourceKey<Biome> SPIRITUAL_DESERT = register("spiritual_desert");

    /**
     * {@link hungteen.imm.ImmortalMod#defferRegister(IEventBus)}
     */
    public static void register(){
//        register(SPIRITUAL_PLAINS, SpiritualLandBiomes.plains());
//        register(SPIRITUAL_SAVANNA, SpiritualLandBiomes.savanna());
//        register(SPIRITUAL_DESERT, SpiritualLandBiomes.desert());
    }

    public static Map<ResourceKey<Biome>, Biome> biomes(){
        return Collections.unmodifiableMap(BIOME_MAP);
    }

    private static void register(ResourceKey<Biome> key, Biome biome){
        BIOME_MAP.put(key, biome);
    }

    private static ResourceKey<Biome> register(String name){
        return BiomeHelper.get().createKey(Util.prefix(name));

    }
}
