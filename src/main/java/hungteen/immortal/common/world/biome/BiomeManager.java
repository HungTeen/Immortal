package hungteen.immortal.common.world.biome;

import hungteen.htlib.util.Pair;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-15 19:00
 **/
public class BiomeManager {

    /**
     * {@link ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void registerSpiritualBiomes(){
        List.of(
                Pair.of(Biomes.LUSH_CAVES, 15)
        ).forEach(pair -> {
            ImmortalAPI.get().registerBiomeSpiritualValue(pair.getFirst(), pair.getSecond());
        });
    }

}
